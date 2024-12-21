<?php

$config['imap_host'] = 'mailserver:143';
$config['smtp_host'] = 'mailserver:587';

$config['db_dsnw'] = 'mysql://roundcube:@localhost/roundcubemail';

// Disable SSL verification
$config['imap_conn_options'] = array (
  'ssl' => 
  array (
    'verify_peer' => false,
    'verify_peer_name' => false,
  ),
);

$config['smtp_conn_options'] = array (
  'ssl' => 
  array (
    'verify_peer' => false,
    'verify_peer_name' => false,
  ),
);

$config['support_url'] = '';
$config['des_key'] = 'q0CGQlZj3yyh1MuUBoC5kHjE';
$config['plugins'] = [];

include(__DIR__ . '/config.docker.inc.php');
