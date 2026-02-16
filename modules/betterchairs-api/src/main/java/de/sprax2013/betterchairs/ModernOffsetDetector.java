package de.sprax2013.betterchairs;

import org.bukkit.block.Block;
import org.bukkit.util.BoundingBox;

import java.util.Collection;

/**
 * Determines sitting offset using {@link Block#getCollisionShape()} and its bounding boxes.
 * Requires Spigot 1.18+.
 */
class ModernOffsetDetector extends OffsetDetector {
    @Override
    protected double getCenterTopY(Block block) {
        Collection<BoundingBox> boxes = block.getCollisionShape().getBoundingBoxes();
        // check 4 points around center, take the lowest result.
        // checking just the center would mess with stair sit height
        double[] samples = {0.49, 0.51};
        double minTopY = Double.MAX_VALUE;

        for (double x : samples) {
            for (double z : samples) {
                // Height at this sample point = top of the highest box covering it
                double pointTopY = boxes.stream()
                        .filter(bb -> bb.getMinX() <= x && bb.getMaxX() >= x && bb.getMinZ() <= z && bb.getMaxZ() >= z)
                        .mapToDouble(BoundingBox::getMaxY)
                        .max()
                        .orElse(0);
                minTopY = Math.min(minTopY, pointTopY);
            }
        }

        return minTopY == Double.MAX_VALUE ? 1.0 : Math.min(minTopY,1.0); // handle fences, so we don't hover above them.
    }

    @Override
    public boolean isSittable(Block block) {
        // if there's not a center to sit on, no good.
         if (getCenterTopY(block) == 0)
             return false;

         //
    }

}
