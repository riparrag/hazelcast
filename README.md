# hazelcast
Hazel Cast Demo

# Levantar Redis en Docker
1. crear una red en docker:
	`docker network create red-docker-ipa-redis`

2. Levantar en docker:
	`docker run --name some-redis --network red-docker-ipa-redis -v "C:/Users/rodri/OneDrive/Documentos/dev/others:/data" -p 6379:6379 -d redis`

4. levantar un cliente redis por consola:
	`docker exec -it some-redis redis-cli`	
5. o entrar a docker desktop a la terminal de redis:
	`redis-cli`

# Levantar Hazelcast en Docker
1.	obtener imagen: 
	`docker pull hazelcast/hazelcast:5.2.3`
2.	Crear red en docker para hazelcast
	`docker network create hazelcast-network`

3.	Crear docker container con Hazelcast
	`docker create --name hazelcast-ipademo --network hazelcast-network -e HZ_CLUSTERNAME=hazelcast-ipademo -e "JAVA_OPTS=-Dhazelcast.local.publicAddress=172.18.0.2" -p 5701:5701 hazelcast/hazelcast:5.2.3`

ó correrlo de una: 
	`docker run -it --network hazelcast-network --rm -e HZ_CLUSTERNAME=hazelcast-ipademo -p 5701:5701 hazelcast/hazelcast:5.2.3`


4. Dentro del terminal del docker de Hazelcast
4.1) Levantar consola: `hz-cli --targets hazelcast-ipademo@172.18.0.2 console`
4.2) Levantar sql: `hz-cli --targets hazelcast-ipademo@172.18.0.2 sql`
4.3) info del cluster: 4.1 Levantar consola: `hz-cli --targets hazelcast-ipademo@172.18.0.2 cluster`

4.	ó en consola Levantar cli con sql
	`docker run --network hazelcast-network -it --rm hazelcast/hazelcast:5.2.3 hz-cli --targets hazelcast-ipademo@172.18.0.2 sql`

mas pruebas: 
	docker exec -it --network hazelcast-network --rm hazelcast/hazelcast:5.2.3 hz-cli --targets hazelcast-ipademo@172.18.0.2:5701
	docker exec hz-cli --targets hazelcast-ipademo@172.18.0.2:5701

# Redis Stack (RedisJSON + RedisSearch)
1. bajar la imagen: `docker pull redis/redis-stack`
2. levantar en docker: 
`docker run -d --name ipa-redis-stack -p 6380:6380 -p 8001:8001 redis/redis-stack:latest`
3. 