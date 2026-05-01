<?php
header("Content-Type: application/json");
require_once __DIR__ . '/../service/ContactServiceEss.php';

// On récupère les données envoyées par Android (en POST)
$id = isset($_POST['id']) ? $_POST['id'] : null;
$name = isset($_POST['name']) ? $_POST['name'] : null;
$phone = isset($_POST['phone']) ? $_POST['phone'] : null;

if ($id && $name && $phone) {
    $service = new ContactServiceEss();
    if ($service->update($id, $name, $phone)) {
        echo json_encode(["status" => "success", "message" => "Contact mis à jour"]);
    } else {
        echo json_encode(["status" => "error", "message" => "Erreur mise à jour"]);
    }
} else {
    echo json_encode(["status" => "error", "message" => "Données incomplètes"]);
}