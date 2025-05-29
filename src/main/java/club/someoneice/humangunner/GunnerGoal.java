package club.someoneice.humangunner;

import com.craftix.hostile_humans.entity.entities.Human;
import com.tacz.guns.api.entity.IGunOperator;
import com.tacz.guns.api.item.builder.GunItemBuilder;
import com.tacz.guns.api.item.gun.AbstractGunItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class GunnerGoal<T extends PathfinderMob> extends Goal {
    protected final T mob;
    protected int seeTime;
    protected ItemStack weapon = GunItemBuilder.create().setId(ResourceLocation.fromNamespaceAndPath("tacz", "glock_17")).build();
    protected final double fireRange;
    protected final IGunOperator operator;
    protected int cooldown = 0;

    public GunnerGoal(T shooter) {
        this.mob = shooter;
        this.fireRange = 56;
        operator = (IGunOperator) this.mob;
    }

    @Override
    public boolean canUse() {
        if (!weapon.equals(this.mob.getMainHandItem())) {
            weapon = this.mob.getMainHandItem();
        }

        if (!(weapon.getItem() instanceof AbstractGunItem gun)) {
            return false;
        }

        gun.setCurrentAmmoCount(this.weapon, 1);

        if (this.mob instanceof Human human && human.isFleeing) {
            return false;
        }

        return this.mob.getTarget() != null;
    }

    @Override
    public boolean canContinueToUse() {
        return this.canUse();
    }

    @Override
    public void start() {
        super.start();
        this.mob.setAggressive(true);
    }

    @Override
    public void stop() {
        super.stop();
        this.seeTime = 0;
        this.mob.setAggressive(false);
    }

    @Override
    public void tick() {
        LivingEntity target = this.mob.getTarget();
        if (target == null || !target.isAlive()) {
            return;
        }

        double distanceToTarget = target.distanceTo(this.mob);
        boolean inRange = distanceToTarget < this.fireRange;
        boolean tooClose = distanceToTarget <= 10;

        if (!inRange) {
            this.mob.getNavigation().moveTo(target, this.mob.getSpeed());
            return;
        }

        if (tooClose) {
            Vec3 vecIn = DefaultRandomPos.getPosAway(this.mob, 16, 4, target.position());
            if (vecIn != null && target.distanceToSqr(vecIn.x, vecIn.y, vecIn.z) >= target.distanceToSqr(this.mob)) {
                var path = this.mob.getNavigation().createPath(vecIn.x, vecIn.y, vecIn.z, 0);
                this.mob.getNavigation().moveTo(path, this.mob.getSpeed() * 1.256);
            }
            return;
        }

        if (!this.mob.canAttack(target)) {
            return;
        }

        this.mob.getLookControl().setLookAt(target);
        // operator.shoot(this.mob::getVoicePitch, this.mob::getYRot);
        ((AbstractGunItem) this.weapon.getItem()).shoot(this.operator.getDataHolder(), this.weapon, this.mob::getXRot, this.mob::getYRot, this.mob);
    }

    protected boolean shouldSuppress() {
        return false;
    }
}
