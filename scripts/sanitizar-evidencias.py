#!/usr/bin/env python3
"""Remove credenciais de evidências textuais antes de publicá-las no CI."""
import html
import json
import os
from pathlib import Path
import re
import sys
import xml.etree.ElementTree as ET

ROOT = Path(__file__).resolve().parents[1]
SECRET_NAME = re.compile(r'(PASSWORD|TOKEN|SECRET|ENCRYPTION_KEY)$', re.I)
JSON_SECRET = re.compile(r'("(?:senha|password|token|csrf|csrfToken|serviceToken|access_token|refresh_token|authorization|cookie|set-cookie)"\s*:\s*)"(?:\\.|[^"\\])*"', re.I)
HEADER_SECRET = re.compile(r'(^[ \t]*(?:Authorization|Cookie|Set-Cookie|X-CSRF-TOKEN|X-Service-Token)\s*[:=]\s*)[^\r\n<]+', re.I | re.M)
QUOTED_HEADER_SECRET = re.compile(r'((?:Authorization|Cookie|Set-Cookie|X-CSRF-TOKEN|X-Service-Token)\s*:\s*)"[^"]*"', re.I)
PARAMETERS = re.compile(r'(Parameters\s*=\s*\{)([^\r\n]*)(\})', re.I)
PARAMETER_SECRET = re.compile(r'((?:_csrf|csrf|senha|password|token|authorization|cookie)\s*=\s*)\[[^\]]*\]', re.I)
PROPERTY_SECRET = re.compile(r'(<property\b[^>]*\bname="([^"]+)"[^>]*\bvalue=")[^"]*(")', re.I)


def secrets():
    values = {value for key, value in os.environ.items() if SECRET_NAME.search(key) and value}
    env_file = ROOT / '.env'
    if env_file.is_file():
        for line in env_file.read_text().splitlines():
            key, separator, value = line.partition('=')
            if separator and SECRET_NAME.search(key.strip()) and value.strip():
                values.add(value.strip().strip('\"\''))
    return values


def sanitize(source, values):
    # Compara também as representações de JSON e XML, preservando o formato do arquivo.
    for value in sorted(values, key=len, reverse=True):
        for encoded in {value, json.dumps(value, ensure_ascii=False)[1:-1], html.escape(value, quote=True)}:
            source = source.replace(encoded, '[REDACTED]')
    source = JSON_SECRET.sub(r'\1"[REDACTED]"', source)
    # JSON serializado dentro de XML/strings de diagnóstico.
    for quote in ['&quot;', '&#34;', '\\"']:
        pattern = re.compile(r'(' + re.escape(quote) + r'(?:senha|password|token|csrf|csrfToken|serviceToken|access_token|refresh_token|authorization|cookie|set-cookie)' + re.escape(quote) + r'\s*:\s*)' + re.escape(quote) + r'.*?' + re.escape(quote), re.I)
        source = pattern.sub(lambda match: match[1] + quote + '[REDACTED]' + quote, source)
    source = PARAMETERS.sub(lambda match: match[1] + PARAMETER_SECRET.sub(r'\1[REDACTED]', match[2]) + match[3], source)
    source = QUOTED_HEADER_SECRET.sub(r'\1"[REDACTED]"', source)
    source = HEADER_SECRET.sub(r'\1[REDACTED]', source)
    return PROPERTY_SECRET.sub(lambda match: match[1] + '[REDACTED]' + match[3] if SECRET_NAME.search(match[2]) else match[0], source)


def verify():
    fixtures = [
        ('Body = {"senha":"fixture-JVM", "nome":"Aluno QA", "status":403}', 'fixture-JVM'),
        ('Body={\\"senha\\":\\"fixture-JVM\\"}', 'fixture-JVM'),
        ('Body={&quot;senha&quot;:&quot;fixture-JVM&quot;}', 'fixture-JVM'),
        ('<property name="ADMIN_PASSWORD" value="fixture-JVM"/>', 'fixture-JVM'),
        ('Authorization: Bearer fixture-JVM\nHTTP 403', 'fixture-JVM'),
        ('env credential=env-secret; tests=11 failures=0', 'env-secret'),
    ]
    for source, secret in fixtures:
        result = sanitize(source, {'env-secret'})
        assert secret not in result
        assert sanitize(result, {'env-secret'}) == result
    result = json.loads(sanitize('{"senha":"fixture-JVM","status":403,"testes":11}', set()))
    assert result == {'senha': '[REDACTED]', 'status': 403, 'testes': 11}
    junit = '''<testsuite tests="11" failures="1" errors="2"><testcase><failure message="HTTP 403">assert esperado201 recebido403; exit=1</failure></testcase><system-out><![CDATA[Body={"senha":"fixture-JVM"}
Cookie: SESSION=fixture-cookie
Authorization: Bearer fixture-auth
]]></system-out><system-err>Body={&quot;senha&quot;:&quot;fixture-xml&quot;}</system-err></testsuite>'''
    cleaned = sanitize(junit, set())
    for secret in ['fixture-JVM', 'fixture-cookie', 'fixture-auth', 'fixture-xml']:
        assert secret not in cleaned
    root = ET.fromstring(cleaned)
    assert root.attrib == {'tests':'11','failures':'1','errors':'2'}
    assert root.find('.//failure').text == 'assert esperado201 recebido403; exit=1'
    assert root.find('.//failure').attrib['message'] == 'HTTP 403'
    result = json.loads(sanitize('{"Authorization":"Bearer fixture-auth","Cookie":"session=fixture-cookie","status":"failed","exit":1}', set()))
    assert result['status'] == 'failed' and result['exit'] == 1
    assert result['Authorization'] == '[REDACTED]' and result['Cookie'] == '[REDACTED]'
    parameter_log = 'Parameters = {_csrf=[csrf-sentinela], senha=[senha-sentinela], teste=[quantidade11]}\nHTTP 403 exit=1'
    cleaned = sanitize(parameter_log, set())
    assert 'csrf-sentinela' not in cleaned and 'senha-sentinela' not in cleaned
    assert 'teste=[quantidade11]' in cleaned and 'HTTP 403 exit=1' in cleaned
    assert sanitize(cleaned, set()) == cleaned
    header_log = 'Headers = [X-CSRF-TOKEN:"csrf-sentinela", X-Service-Token:"servico-sentinela"]\nHTTP 403 exit=1'
    cleaned = sanitize(header_log, set())
    assert 'csrf-sentinela' not in cleaned and 'servico-sentinela' not in cleaned
    assert 'HTTP 403 exit=1' in cleaned
    assert json.loads(sanitize('{"csrfToken":"csrf-sentinela","status":403}', set())) == {'csrfToken':'[REDACTED]','status':403}
    print('PASS sanitização de JSON/XML/CDATA/headers/Parameters/ambiente, preservação de falhas e idempotência')


def main():
    if '--verificar' in sys.argv:
        verify()
        return
    values = secrets()
    files = set((ROOT / 'evidence').rglob('*'))
    for pattern in ['*/target/surefire-reports/*', '*/target/failsafe-reports/*']:
        files.update((ROOT / 'services').glob(pattern))
    count = 0
    for path in sorted(files):
        if not path.is_file() or path.suffix.lower() not in {'.log', '.xml', '.txt', '.json'}:
            continue
        source = path.read_text(encoding='utf-8', errors='replace')
        result = sanitize(source, values)
        if result != source:
            path.write_text(result, encoding='utf-8')
            count += 1
    print(f'Evidências textuais saneadas: {count} arquivo(s)')


if __name__ == '__main__':
    main()
