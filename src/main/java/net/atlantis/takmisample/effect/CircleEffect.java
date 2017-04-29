package net.atlantis.takmisample.effect;

import io.reactivex.Observable;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

public class CircleEffect {
    private Player player;
    private Vector vector;
    private Location initialLocation;
    private double targetAngle;
    private double targetAngle2;
    private double radius;

    /**
     * エフェクト描画に必要な変数を計算する
     *
     * @param player エフェクトを実行したプレイヤー
     */
    public CircleEffect(Player player) {
        this.player = player;
        initialLocation = player.getEyeLocation();
        vector = player.getEyeLocation().getDirection();
        initialLocation.add(vector.getX() * 2.0, vector.getY() * 2.0, vector.getZ() * 2.0);
        targetAngle = Math.atan(vector.getZ() / vector.getX()) + Math.PI / 2;

        //反転させる
        double rate = 1.0;
        if (vector.getX() > 0) {
            rate = -1.0;
        }
        //X-Z平面に対してのYの角度を求める
        targetAngle2 = rate * Math.atan(vector.getY() / Math.sqrt(Math.pow(vector.getX(), 2) + Math.pow(vector.getZ(), 2)));
        radius = 2.0;
    }

    public void draw() {
        player.getWorld().playSound(initialLocation, Sound.ENTITY_SHULKER_TELEPORT, 3.0F, 0.933F);
        Observable.interval(10, TimeUnit.MILLISECONDS)
                .takeWhile(val -> val < 130)
                .subscribe(data -> {
                    drawEffect(data);
                });
    }

    /**
     * エフェクトを描く
     *
     * @param data data
     */
    private void drawEffect(Long data) {
        double angle = 2 * Math.PI * data / 120;
        if (data <= 60) {
            drawParticle(radius, angle);
            drawParticle(radius, angle + 2 * Math.PI / 3);
            drawParticle(radius, angle + 4 * Math.PI / 3);
        } else if (data <= 120) {
            double currentRadius = radius * (120 - data) / 60;
            drawParticle(currentRadius, angle);
            drawParticle(currentRadius, angle + 2 * Math.PI / 3);
            drawParticle(currentRadius, angle + 4 * Math.PI / 3);
        } else if (data <= 121) {
            player.getWorld().playSound(initialLocation, Sound.ENTITY_FIREWORK_LAUNCH, 3.0F, 0.933F);

        } else {
            drawLine();
        }
    }

    /**
     * 円を描く
     *
     * @param radius 円の半径
     * @param angle  円の角度
     */
    private void drawParticle(double radius, double angle) {
        double x = Math.cos(angle) * radius;
        double y = Math.sin(angle) * radius;
        double z = 0.0;

        Location currentLocation = getTransformedLocation(initialLocation.clone(), x, y, z);

        // 魔法陣を発生させる
        player.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, currentLocation, 15);
    }

    /**
     * 線を引く
     */
    private void drawLine() {
        double x = 0.0;
        double y = 0.0;
        double z = 0.0;

        Location currentLocation = getTransformedLocation(initialLocation.clone(), x, y, z);

        for (int i = 0; i < 30; i++) {
            player.getWorld().spawnParticle(Particle.REDSTONE, currentLocation, 10);
            if (i > 28) {
                player.getWorld().spawnParticle(Particle.FLAME, currentLocation, 10);
            }

            dealDamage(currentLocation);
            currentLocation.add(vector);
        }
    }

    /**
     * 範囲内のLivingEntityにダメージを与える
     *
     * @param location 指定座標
     */
    private void dealDamage(Location location) {
        Collection<Entity> entities = player.getWorld().getNearbyEntities(location, 1.0, 1.0, 1.0);
        if (!entities.isEmpty()) {
            entities.forEach(entity -> {
                if (entity instanceof LivingEntity) {
                    LivingEntity livingEntity = (LivingEntity) entity;
                    livingEntity.damage(20.0);
                }
            });
        }
    }

    /**
     * 目線のベクトルを標準とした座標系へ変換する
     *
     * @param location 現在の位置
     * @param x        基底座標でのx
     * @param y        基底座標でのy
     * @param z        基底座標でのz
     * @return 座標変換されたLocation
     */
    private Location getTransformedLocation(Location location, double x, double y, double z) {
        double z1 = z * Math.cos(targetAngle2) - y * Math.sin(targetAngle2);
        double y1 = z * Math.sin(targetAngle2) + y * Math.cos(targetAngle2);

        double x1 = x * Math.cos(targetAngle) - z1 * Math.sin(targetAngle);
        double z2 = x * Math.sin(targetAngle) + z1 * Math.cos(targetAngle);

        // 位置の調整
        return location.add(x1, y1, z2);
    }
}
