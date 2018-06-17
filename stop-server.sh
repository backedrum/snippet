#!/usr/bin/env bash

echo "Stopping application.."
kill $(ps aux | grep '[s]nippet' | awk '{print $2}')
