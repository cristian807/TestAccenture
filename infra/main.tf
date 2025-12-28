terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 5.0"
    }
  }
}

provider "aws" {
  region = var.aws_region
}

resource "aws_db_instance" "mysql_db" {
  identifier = "tets-accenture"

  engine         = "mysql"
  engine_version = "8.0"

  instance_class = "db.t3.micro"

  allocated_storage = 20
  storage_type      = "gp2"

  db_name  = "accenture_db"
  username = var.db_username
  password = var.db_password

  port = 3306

  publicly_accessible = true
  skip_final_snapshot = true

  deletion_protection = false
}
