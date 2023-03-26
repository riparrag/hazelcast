package com.ipasoft.hazelcast;

import redis.clients.jedis.Jedis;

public class JedisTest {
    public static void main(String[] args) {
        // Crea un objeto Jedis y especifica el nombre de host y el puerto de Redis
        Jedis jedis = new Jedis("127.0.0.1", 6379);

        // Ejecuta comandos Redis en el objeto Jedis
        jedis.set("foo", "bar");
        String value = jedis.get("foo");
        System.out.println(value);

        // Cierra la conexión
        jedis.close();
    }
}