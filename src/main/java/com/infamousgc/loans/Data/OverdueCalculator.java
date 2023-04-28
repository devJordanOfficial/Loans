package com.infamousgc.loans.Data;

import net.luckperms.api.context.ContextCalculator;
import net.luckperms.api.context.ContextConsumer;
import net.luckperms.api.context.ContextSet;
import net.luckperms.api.context.ImmutableContextSet;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;

public class OverdueCalculator implements ContextCalculator<Player> {
    @Override
    public void calculate(@NonNull Player target, @NonNull ContextConsumer consumer) {
        consumer.accept("overdue", Players.get(target).isOverdueString());
    }

    @Override
    public ContextSet estimatePotentialContexts() {
        ImmutableContextSet.Builder builder = ImmutableContextSet.builder();
        builder.add("overdue", "true");
        builder.add("overdue", "false");
        return builder.build();
    }
}
