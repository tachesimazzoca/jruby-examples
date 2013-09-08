<?php

class Renderer
{
    var $template;
    var $params = array();

    function render()
    {
        ob_start();
        include $this->template;
        $output = ob_get_contents();
        ob_end_clean();

        return $output;
    }
}
