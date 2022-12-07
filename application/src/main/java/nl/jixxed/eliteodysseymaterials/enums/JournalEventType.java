package nl.jixxed.eliteodysseymaterials.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum JournalEventType {
    COMMANDER("Commander"),
    ENGINEERPROGRESS("EngineerProgress"),
    EMBARK("Embark"),
    SHIPLOCKER("ShipLocker"),
    BACKPACK("Backpack"),
    CAPI_FLEETCARRIER("CapiFleetCarrier"),//CAPI Resource
    BACKPACKCHANGE("BackpackChange"),
    RESUPPLY("Resupply"),
    FSDJUMP("FSDJump"),
    LOCATION("Location"),
    DOCKED("Docked"),
    TOUCHDOWN("Touchdown"),
    UNDOCKED("Undocked"),
    LIFTOFF("Liftoff"),
    APPROACHBODY("ApproachBody"),
    APPROACHSETTLEMENT("ApproachSettlement"),
    LEAVEBODY("LeaveBody"),
    SUPERCRUISEENTRY("SupercruiseEntry"),
    LOADGAME("LoadGame"),
    UNKNOWN("Unknown"),
    MATERIALS("Materials"),
    MATERIALCOLLECTED("MaterialCollected"),
    MATERIALTRADE("MaterialTrade"),
    MATERIALDISCARDED("MaterialDiscarded"),
    ENGINEERCONTRIBUTION("EngineerContribution"),
    ENGINEERCRAFT("EngineerCraft"),
    MISSIONCOMPLETED("MissionCompleted"),
    SYNTHESIS("Synthesis"),
    UPGRADESUIT("UpgradeSuit"),
    UPGRADEWEAPON("UpgradeWeapon"),
    RECEIVETEXT("ReceiveText"),
    SENDTEXT("SendText"),
    BUYMICRORESOURCES("BuyMicroResources"),
    MARKETBUY("MarketBuy"),
    MARKETSELL("MarketSell"),
    CARGO("Cargo"),
    CARGOTRANSFER("CargoTransfer"),
    TECHNOLOGYBROKER("TechnologyBroker"),
    SUITLOADOUT("SuitLoadout"),
    SWITCHSUITLOADOUT("SuitLoadout");
    private final String name;

    public static JournalEventType forName(final String name) {
        try {
            return JournalEventType.valueOf(name.toUpperCase());
        } catch (final IllegalArgumentException ex) {
            return JournalEventType.UNKNOWN;
        }
    }

    public String friendlyName() {
        return this.name;
    }
}
