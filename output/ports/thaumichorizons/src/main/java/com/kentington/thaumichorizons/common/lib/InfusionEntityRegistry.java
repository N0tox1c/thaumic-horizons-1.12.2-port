package com.kentington.thaumichorizons.common.lib;
public final class InfusionEntityRegistry {
    private InfusionEntityRegistry() {}
    public static net.minecraft.util.ResourceLocation idForLegacy(int legacyId) {
        switch(legacyId) {
            case 0: return new net.minecraft.util.ResourceLocation("thaumichorizons", "primedalchemite");
            case 1: return new net.minecraft.util.ResourceLocation("thaumichorizons", "egg");
            case 2: return new net.minecraft.util.ResourceLocation("thaumichorizons", "chromaticsheep");
            case 3: return new net.minecraft.util.ResourceLocation("thaumichorizons", "selfshearingsheep");
            case 4: return new net.minecraft.util.ResourceLocation("thaumichorizons", "guardianpanther");
            case 5: return new net.minecraft.util.ResourceLocation("thaumichorizons", "gravekeeper");
            case 6: return new net.minecraft.util.ResourceLocation("thaumichorizons", "familiar");
            case 7: return new net.minecraft.util.ResourceLocation("thaumichorizons", "chocolatecow");
            case 9: return new net.minecraft.util.ResourceLocation("thaumichorizons", "scholarchicken");
            case 10: return new net.minecraft.util.ResourceLocation("thaumichorizons", "goldchicken");
            case 11: return new net.minecraft.util.ResourceLocation("thaumichorizons", "orepig");
            case 12: return new net.minecraft.util.ResourceLocation("thaumichorizons", "tainteaterpig");
            case 13: return new net.minecraft.util.ResourceLocation("thaumichorizons", "endersteed");
            case 14: return new net.minecraft.util.ResourceLocation("thaumichorizons", "seawolf");
            case 15: return new net.minecraft.util.ResourceLocation("thaumichorizons", "netherhound");
            case 16: return new net.minecraft.util.ResourceLocation("thaumichorizons", "lunarwolf");
            case 17: return new net.minecraft.util.ResourceLocation("thaumichorizons", "syringe");
            case 18: return new net.minecraft.util.ResourceLocation("thaumichorizons", "blastphial");
            case 19: return new net.minecraft.util.ResourceLocation("thaumichorizons", "golemth");
            case 20: return new net.minecraft.util.ResourceLocation("thaumichorizons", "horsezombie");
            case 21: return new net.minecraft.util.ResourceLocation("thaumichorizons", "horseskeleton");
            case 22: return new net.minecraft.util.ResourceLocation("thaumichorizons", "nightmareth");
            case 23: return new net.minecraft.util.ResourceLocation("thaumichorizons", "boatgreatwood");
            case 24: return new net.minecraft.util.ResourceLocation("thaumichorizons", "boatthaumium");
            case 25: return new net.minecraft.util.ResourceLocation("thaumichorizons", "meatslime");
            case 26: return new net.minecraft.util.ResourceLocation("thaumichorizons", "mercurialslime");
            case 27: return new net.minecraft.util.ResourceLocation("thaumichorizons", "voltslime");
            case 28: return new net.minecraft.util.ResourceLocation("thaumichorizons", "medslime");
            case 29: return new net.minecraft.util.ResourceLocation("thaumichorizons", "sheeder");
            case 30: return new net.minecraft.util.ResourceLocation("thaumichorizons", "lostsoul");
            case 31: return new net.minecraft.util.ResourceLocation("thaumichorizons", "lightningboltfinite");
            default: throw new IllegalArgumentException("Unknown legacy infusion entity " + legacyId);
        }
    }
}
