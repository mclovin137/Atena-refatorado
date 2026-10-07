<?php
require_once __DIR__ . '/config.php';
require_once __DIR__ . '/Models/Arquivo.php';
Arquivo::baixar((string) ($_GET['nome'] ?? ''));
