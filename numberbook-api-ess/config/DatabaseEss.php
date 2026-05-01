<?php
class DatabaseEss {
    private $host = "localhost";
    private $dbName = "numberbook_ess"; // Base avec ta signature
    private $username = "root";
    private $password = "";
    public $conn;

    public function getConnection() {
        $this->conn = null;
        try {
            $this->conn = new PDO(
                "mysql:host=" . $this->host . ";dbname=" . $this->dbName . ";charset=utf8mb4",
                $this->username,
                $this->password
            );
            $this->conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
        } catch (PDOException $exception) {
            // Signature ess dans le log d'erreur
            error_log("Erreur de connexion [ess]: " . $exception->getMessage());
            echo json_encode(["error" => "Indisponibilité du serveur ess"]);
        }
        return $this->conn;
    }
}
?>