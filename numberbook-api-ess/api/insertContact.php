<?php
header("Content-Type: application/json; charset=UTF-8");
require_once __DIR__ . '/../service/ContactServiceEss.php';

// Récupérer les données JSON envoyées par l'application
$data = json_decode(file_get_contents("php://input"), true);

if (isset($data['ess_name']) && isset($data['ess_phone'])) {
    $service = new ContactServiceEss();
    // On passe le nom, le téléphone et la source "mobile_ess"
    $success = $service->insert($data['ess_name'], $data['ess_phone'], "mobile_ess");
    
    if ($success) {
        echo json_encode(["status" => "success", "message" => "Contact synchronisé"]);
    } else {
        echo json_encode(["status" => "error", "message" => "Erreur d'insertion"]);
    }
} else {
    echo json_encode(["status" => "error", "message" => "Données incomplètes"]);
}
?>