#!/usr/bin/env php
<?php

error_reporting(E_ALL);

require_once dirname(__FILE__) . '/Renderer.php';

$params = array();
for ($i = 0; $i < 20; $i++) {
    $params[] = array(
        'title' => 'foo',
        'description' => 'bar',
    );
}
$template = dirname(__FILE__) . '/template.php';

$start = microtime(true);
for ($i = 0; $i < 1000; $i++) {
    $renderer = new Renderer();
    $renderer->template = $template;
    $renderer->params = $params;
    echo $renderer->render();
}
$end = microtime(true);

echo ($end - $start) . "\n";
