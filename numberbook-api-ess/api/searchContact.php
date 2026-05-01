<?php
header("Content-Type: application/json; charset=UTF-8");
require_once __DIR__ . '/../service/ContactServiceEss.php';

// On récupère le mot-clé envoyé par l'application
$keyword = isset($_GET['keyword']) ? $_GET['keyword'] : '';

$service = new ContactServiceEss();
$result = $service->search($keyword);

// Très important : toujours renvoyer un tableau JSON
echo json_encode($result ? $result : []);
?>