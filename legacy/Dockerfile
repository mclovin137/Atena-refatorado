FROM php:8.4-apache AS base
RUN apt-get update && apt-get install -y --no-install-recommends libpq-dev libpng-dev libjpeg62-turbo-dev libonig-dev unzip \
    && docker-php-ext-configure gd --with-jpeg \
    && docker-php-ext-install -j2 pdo_pgsql gd mbstring calendar \
    && a2enmod rewrite \
    && rm -rf /var/lib/apt/lists/*
RUN sed -i 's/Listen 80/Listen 8080/' /etc/apache2/ports.conf \
    && sed -i 's/<VirtualHost \*:80>/<VirtualHost *:8080>/' /etc/apache2/sites-available/000-default.conf
COPY infra/apache.conf /etc/apache2/conf-available/atena.conf
COPY infra/php.ini /usr/local/etc/php/conf.d/atena.ini
RUN a2enconf atena
FROM base AS dependencias
COPY --from=composer:2 /usr/bin/composer /usr/local/bin/composer
WORKDIR /dependencias
COPY composer.json composer.lock ./
RUN composer install --no-dev --no-interaction --prefer-dist --no-progress --no-scripts --optimize-autoloader
FROM base AS runtime
WORKDIR /var/www/html
COPY . .
COPY --from=dependencias /dependencias/vendor ./Lib/vendor
RUN mkdir -p /tmp/atena-mpdf /tmp/atena-sessions \
    && chown -R www-data:www-data /tmp/atena-mpdf /tmp/atena-sessions \
    && chmod 700 /tmp/atena-mpdf /tmp/atena-sessions \
    && chown -R www-data:www-data /var/run/apache2 /var/lock/apache2 /var/log/apache2
USER www-data
EXPOSE 8080
HEALTHCHECK --interval=30s --timeout=3s CMD php -r '$s=fsockopen("127.0.0.1",8080,$e,$m,2);if(!$s)exit(1);fwrite($s,"GET /health.php HTTP/1.0\r\nHost: localhost\r\n\r\n");exit(str_contains(fgets($s),"200")?0:1);'
