#!/usr/bin/env bash

echo "Stopping application.."
kill $(ps aux | grep '[s]nippet' | awk '{print $2}')

echo "Removing docker container.."
docker rm -f snippet

echo "Done!"