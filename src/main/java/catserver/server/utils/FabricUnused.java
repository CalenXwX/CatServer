package catserver.server.utils;

import catserver.server.CatServer;

import java.util.HashMap;

public class FabricUnused {
    public static class FabricUnusedThreadLocal<T> extends ThreadLocal<T> {
        @Override
        public void set(T value) {
            CatServer.LOGGER.warn("This field shouldn't be used!", new RuntimeException());
            super.set(value);
        }

        @Override
        public T get() {
            CatServer.LOGGER.warn("This field shouldn't be used!", new RuntimeException());
            return super.get();
        }

        @Override
        public void remove() {
            CatServer.LOGGER.warn("This field shouldn't be used!", new RuntimeException());
            super.remove();
        }
    }

    public static class FabricUnusedMap<K, V> extends HashMap<K, V> {
        @Override
        public V put(K key, V value) {
            CatServer.LOGGER.warn("This field shouldn't be used!", new RuntimeException());
            return super.put(key, value);
        }

        @Override
        public V get(Object key) {
            CatServer.LOGGER.warn("This field shouldn't be used!", new RuntimeException());
            return super.get(key);
        }

        @Override
        public V remove(Object key) {
            CatServer.LOGGER.warn("This field shouldn't be used!", new RuntimeException());
            return super.remove(key);
        }

        @Override
        public boolean remove(Object key, Object value) {
            CatServer.LOGGER.warn("This field shouldn't be used!", new RuntimeException());
            return super.remove(key, value);
        }
    }
}
