<?php
header("Content-Type: application/json");
require_once __DIR__ . '/../service/ContactServiceEss.php';

// On vérifie si l'ID est bien envoyé dans l'URL (ex: deleteContact.php?id=5)
$id = isset($_GET['id']) ? $_GET['id'] : null;

if ($id) {
    $service = new ContactServiceEss();
    $success = $service->delete($id);

    if ($success) {
        echo json_encode(["status" => "success", "message" => "Contact supprimé"]);
    } else {
        echo json_encode(["status" => "error", "message" => "Erreur lors de la suppression"]);
    }
} else {
    echo json_encode(["status" => "error", "message" => "ID manquant"]);
}
?>