#!/usr/bin/env bash

set -eo pipefail

modules=( business chatting gateway oauth )

for module in "${modules[@]}"; do
    docker build -t "${module}:latest" /home/admin/application/${module}/
done
