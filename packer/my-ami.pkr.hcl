packer {
  required_plugins {
    amazon = {
      version = ">= 1.0.0"
      source  = "github.com/hashicorp/amazon"
    }
  }
}

variable "aws_region" {
  type    = string
  default = "us-east-1"
}

variable "source_ami" {
  type    = string
  default = "ami-06db4d78cb1d3bbf9"
}

variable "instance_type" {
  type    = string
  default = "t2.micro"
}

variable "subnet_id" {
  type    = string
  default = "subnet-0a325fe35bf0b984c"
}

variable "ssh_username" {
  type    = string
  default = "admin"
}

build {
  sources = ["source.amazon-ebs.my-ami"]
  provisioner "shell" {
    environment_vars = [
      "DEBIAN_FRONTEND=noninteractive",
      "CHECKPOINT_DISABLE=1",
    ]
    inline = [
      "sudo apt-get update",
      "sudo apt-get upgrade -y",
      "sudo apt-get install nginx -y",
      "sudo apt-get install mariadb-server -y",
      "sudo service mysql start",
      "sudo mysql -u root -e \"CREATE DATABASE csye6225_assignments;\"",
      "sudo apt-get install openjdk-17-jre -y",
      "export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64",
      "export PATH=$JAVA_HOME/bin:$PATH",
      "sudo apt-get install maven -y"

    ]
  }
}

source "amazon-ebs" "my-ami" {
  source_ami      = var.source_ami
  ami_name        = "csye6225_f23_${formatdate("YYYY_MM_DD_hh_mm_ss", timestamp())}"
  ami_description = "AMI for csye6225"
  instance_type   = var.instance_type
  region          = var.aws_region
  ssh_username    = var.ssh_username
  subnet_id       = var.subnet_id

  ami_regions = ["us-east-1"]

  aws_polling {
    delay_seconds = 120
    max_attempts  = 50
  }

  ami_users = ["292674977374", "372182193019"]

  launch_block_device_mappings {
    delete_on_termination = true
    device_name           = "/dev/xvda"
    volume_size           = 8
    volume_type           = "gp2"
  }
}
