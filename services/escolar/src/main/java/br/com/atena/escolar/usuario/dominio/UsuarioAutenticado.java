package br.com.atena.escolar.usuario.dominio;

public record UsuarioAutenticado(Long identificador, String email, PerfilEnum perfil) {

    public boolean secretaria() {
        return perfil == PerfilEnum.SECRETARIA;
    }

    public boolean professor() {
        return perfil == PerfilEnum.PROFESSOR;
    }

    public boolean aluno() {
        return perfil == PerfilEnum.ALUNO;
    }
}
