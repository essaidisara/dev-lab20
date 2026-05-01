<?php
require_once __DIR__ . '/../config/DatabaseEss.php';

class ContactServiceEss {
    private $conn;
    private $table = "ess_contact";

    public function __construct() {
        $database = new DatabaseEss();
        $this->conn = $database->getConnection();
    }

    public function insert($name, $phone, $source = "mobile_ess") {
        $check = "SELECT id_ess FROM " . $this->table . " WHERE ess_phone = :phone LIMIT 1";
        $stmtCheck = $this->conn->prepare($check);
        $stmtCheck->execute([':phone' => $phone]);
        if($stmtCheck->rowCount() > 0) return true; 

        $sql = "INSERT INTO " . $this->table . " (ess_name, ess_phone, ess_source) VALUES (:name, :phone, :source)";
        $stmt = $this->conn->prepare($sql);
        return $stmt->execute([':name' => $name, ':phone' => $phone, ':source' => $source]);
    }

    public function search($keyword) {
        try {
            // On sélectionne bien id_ess pour Android
            $query = "SELECT id_ess, ess_name, ess_phone FROM " . $this->table . " WHERE ess_name LIKE :keyword";
            $stmt = $this->conn->prepare($query);
            $searchParam = $keyword . '%';
            $stmt->execute(['keyword' => $searchParam]);

            // FETCH_ASSOC simple pour éviter l'erreur serveur sur Android
            return $stmt->fetchAll(PDO::FETCH_ASSOC);
        } catch (PDOException $e) {
            return [];
        }
    }

    public function update($id, $name, $phone) {
        try {
            $sql = "UPDATE " . $this->table . " SET ess_name = :name, ess_phone = :phone WHERE id_ess = :id";
            $stmt = $this->conn->prepare($sql);
            return $stmt->execute([':name' => $name, ':phone' => $phone, ':id' => $id]);
        } catch (PDOException $e) {
            return false;
        }
    }

    public function delete($id) {
        try {
            // Utilisation de id_ess pour la suppression
            $sql = "DELETE FROM " . $this->table . " WHERE id_ess = :id";
            $stmt = $this->conn->prepare($sql);
            return $stmt->execute([':id' => $id]);
        } catch (PDOException $e) {
            return false;
        }
    }
}