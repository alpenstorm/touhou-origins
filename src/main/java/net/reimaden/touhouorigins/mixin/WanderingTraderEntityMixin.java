package net.reimaden.touhouorigins.mixin;

import io.github.apace100.apoli.component.PowerHolderComponent;
import net.reimaden.touhouorigins.power.ModifyBehaviorPower;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.FleeEntityGoal;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.passive.WanderingTraderEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(WanderingTraderEntity.class)
public abstract class WanderingTraderEntityMixin extends MerchantEntity {

    protected WanderingTraderEntityMixin(EntityType<? extends MerchantEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "initGoals", at = @At(value = "RETURN"))
    public void initPlayerZombieGoals(CallbackInfo info) {
        this.goalSelector.add(1, new FleeEntityGoal<>(this, PlayerEntity.class, 8.0f, 0.5, 0.5, (entity) -> {
            if(entity instanceof PlayerEntity player) {
                List<ModifyBehaviorPower> powers = PowerHolderComponent.getPowers(player, ModifyBehaviorPower.class);
                powers.removeIf((power) -> !power.checkEntity(EntityType.WANDERING_TRADER));

                if (!powers.isEmpty()) {
                    ModifyBehaviorPower.EntityBehavior behavior = powers.get(0).getDesiredBehavior();
                    return behavior == ModifyBehaviorPower.EntityBehavior.HOSTILE;
                }
            }
            return false;
        }));
    }
}
