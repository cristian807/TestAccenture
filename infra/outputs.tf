output "db_endpoint" {
  description = "Endpoint de conexión a la base de datos"
  value       = aws_db_instance.mysql_db.endpoint
}
