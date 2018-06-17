#!/usr/bin/env bash

SNIPPET_CONTAINER='snippet'
COUNT=$(docker ps -a | grep "$SNIPPET_CONTAINER" | wc -l)
if (($COUNT == 0)); then
    echo "Starting Postgres docker container.."
    docker run --name snippet -d -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=snippet -p 5432:5432 postgres:10.3
fi

echo "Building and starting Snippet server.."
run_snippet="mvn clean compile exec:java -Dsnippet"
snippet_log=snippet-$(date +%FT%T).log
nohup $run_snippet > $snippet_log 2>&1 &

if (($COUNT == 0)); then

echo "Waiting for a DB to be initialized.."
tail -f $snippet_log | while read LOGLINE
do
   [[ "${LOGLINE}" == *"Initialized JPA EntityManagerFactory"* ]] && pkill -P $$ tail
done

echo "Setting up admin credentials for Snippet.."
read -p "Please specify admin user name:" username
read -sp "Specify password:" password
base64_pass=`printf $password | base64`
createUser="insert into users (datetime, username, password) values(now()::timestamp, '$username', '$base64_pass');"
docker exec -it snippet psql -U postgres -d snippet -c "$createUser"
fi

if [[ "$OSTYPE" == "linux-gnu" ]]; then
        python -mwebbrowser http://localhost:8080
elif [[ "$OSTYPE" == "darwin"* ]]; then
        open http://localhost:8080
elif [[ "$OSTYPE" == "freebsd"* ]]; then
        python -mwebbrowser http://localhost:8080
else
        echo "Please navigate to http://localhost:8080 and use your Snippet admin credentials to login"
fi
