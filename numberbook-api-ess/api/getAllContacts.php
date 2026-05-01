<?php
// Signature : ess
header("Content-Type: application/json; charset=UTF-8");

// Appel du service personnalisé ess
require_once __DIR__ . '/../service/ContactServiceEss.php';

$service = new ContactServiceEss();
$result = $service->getAll();

// On retourne le tableau associatif converti en JSON
echo json_encode($result);
?>