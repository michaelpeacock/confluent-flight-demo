./prepTopics.sh

curl localhost:8083/connectors -X POST -H "Content-Type: application/json" -d @opensky-connect.json

../ksql/prepKsql.sh