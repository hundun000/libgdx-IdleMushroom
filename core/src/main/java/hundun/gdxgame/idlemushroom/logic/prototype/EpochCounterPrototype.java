package hundun.gdxgame.idlemushroom.logic.prototype;

import hundun.gdxgame.gamelib.base.util.JavaFeatureForGwt;
import hundun.gdxgame.idlemushroom.logic.construction.BaseIdleDemoConstruction;
import hundun.gdxgame.idlemushroom.logic.DemoBuiltinConstructionsLoader;
import hundun.gdxgame.idlemushroom.logic.IdleMushroomConstructionPrototypeId;
import hundun.gdxgame.idlemushroom.logic.ResourceType;
import hundun.gdxgame.idlemushroom.util.IdleMushroomJavaFeatureForGwt;
import hundun.gdxgame.idleshare.gamelib.framework.model.construction.AbstractConstructionPrototype;
import hundun.gdxgame.idleshare.gamelib.framework.model.construction.base.BaseConstruction;
import hundun.gdxgame.idleshare.gamelib.framework.model.construction.base.DescriptionPackage;
import hundun.gdxgame.idleshare.gamelib.framework.model.construction.base.DescriptionPackageFactory;
import hundun.gdxgame.idleshare.gamelib.framework.model.construction.starter.ConstProficiencyComponent;
import hundun.gdxgame.idleshare.gamelib.framework.model.construction.starter.SimpleAutoOutputComponent;
import hundun.gdxgame.idleshare.gamelib.framework.model.grid.GridPosition;
import hundun.gdxgame.idleshare.gamelib.framework.util.text.Language;



public class EpochCounterPrototype extends AbstractConstructionPrototype {
    public static DescriptionPackage descriptionPackageEN = DescriptionPackage.builder()
            .upgradeButtonText("Upgrade")
            .upgradeCostDescriptionStart("Upgrade cost")
            .upgradeMaxLevelDescription("(max)")
            .levelDescriptionProvider(DescriptionPackageFactory.ONLY_LEVEL_IMP)
            .proficiencyDescriptionProvider(DescriptionPackageFactory.EN_PROFICIENCY_IMP)
            .extraTexts(JavaFeatureForGwt.listOf(
                    "Enlargement: ",
                    "Tile max level: "
            ))
            .build();


    public static DescriptionPackage descriptionPackageCN = DescriptionPackage.builder()
            .upgradeButtonText("升级")
            .upgradeCostDescriptionStart("升级费用")
            .upgradeMaxLevelDescription("(已达到最大等级)")
            .levelDescriptionProvider(DescriptionPackageFactory.ONLY_LEVEL_IMP)
            .proficiencyDescriptionProvider(DescriptionPackageFactory.CN_PROFICIENCY_IMP)
            .extraTexts(JavaFeatureForGwt.listOf(
                    "巨大化: ",
                    "地块等级上限: "
            ))
            .build();

    public EpochCounterPrototype(Language language) {
        super(
                IdleMushroomConstructionPrototypeId.EPOCH_COUNTER,
                language
        );
        switch (language)
        {
            case CN:
                this.descriptionPackage = descriptionPackageCN;
                break;
            default:
                this.descriptionPackage = descriptionPackageEN;
                break;
        }

    }

    @Override
    public BaseConstruction getInstance(GridPosition position) {
        String id = prototypeId + "_" + IdleMushroomJavaFeatureForGwt.uuid();

        BaseIdleDemoConstruction thiz = new BaseIdleDemoConstruction(prototypeId, id, position, descriptionPackage);

        ConstProficiencyComponent proficiencyComponent = new ConstProficiencyComponent(thiz);
        thiz.setProficiencyComponent(proficiencyComponent);

        SimpleAutoOutputComponent outputComponent = new SimpleAutoOutputComponent(thiz);
        thiz.setOutputComponent(outputComponent);

        thiz.getUpgradeComponent().setUpgradeCostPack(DemoBuiltinConstructionsLoader.toPack(JavaFeatureForGwt.mapOf(
                ResourceType.DNA_POINT, 0
        )));
        thiz.getUpgradeComponent().setCalculateCostFunction((baseValue, level) -> {
            switch (level) {
                case 1:
                    return 1024L;
                case 2:
                    return 1024 * 1024L;
                default:
                    return 1L;
            }
        });
        thiz.getLevelComponent().maxLevel = 3;

        return thiz;
    }
}
