#!/usr/bin/env bash

set -eo pipefail

modules=( business chatting gateway oauth admin )

for module in "${modules[@]}"; do
    docker build -t "${module}:latest" /home/admin/application/${module}/
done
