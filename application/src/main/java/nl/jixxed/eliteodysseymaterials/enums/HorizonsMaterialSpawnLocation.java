package nl.jixxed.eliteodysseymaterials.enums;

public enum HorizonsMaterialSpawnLocation implements SpawnLocation {
    HOT_JUPITER,
    DAVS_HOPE,
    BRAIN_TREES,
    JAMESON_CRASH_SITE,
    MISSION_REWARD,
    SCAN_MISSIONS,

    HGE_FEDERATION,
    HGE_EMPIRE,
    HGE_CIVIL_UNREST,
    HGE_WAR_CIVIL_WAR,
    HGE_BOOM_EXPANSION,
    HGE_OUTBREAK,

    CRYSTAL_SHARDS_POLONIUM,
    CRYSTAL_SHARDS_RUTHENIUM_A,
    CRYSTAL_SHARDS_RUTHENIUM_B,
    CRYSTAL_SHARDS_TELLURIUM,
    CRYSTAL_SHARDS_TECHNETIUM,
    CRYSTAL_SHARDS_YTTRIUM,
    CRYSTAL_SHARDS_ANTIMONY,
    RING_ICE,
    RING_ALL,
    RING_PRISTINE,
    ASTEROIDS,
    CRASHED_SHIP,
    SURFACE_PROSPECTING,
    BASIC_SCAN_COMBAT_PIRATE,
    BASIC_SCAN_HAULAGE,
    BASIC_SCAN_AUTHORITY_MILITARY,
    WAKE_SCAN_HIGH,
    MEGA_SHIP_HACKING,
    SETTLEMENT_DATAPOINT,
    ENCODED_EMISSIONS_DATA_BEACON,
    DEGRADED_EMISSIONS_DATA_BEACON,
    THARGOID_INTERCEPTOR_XENOSCAN,
    THARGOID_SCOUT_XENOSCAN,
    THARGOID_UPLINK_DEVICE_SCAN,
    THARGOID_SCAVENGER_SCAN,
    THARGOID_WAKE_SCAN,
    GUARDIAN_DATA_TERMINAL,
    GUARDIAN_OBELISK,
    THARGOID_SCAVENGER,
    THARGOID_SENSOR,
    THARGOID_SHIP,
    GUARDIAN_PANELS,
    GUARDIAN_SENTINELS,
    ENCODED_EMISSIONS_ANARCHY,
    ENCODED_EMISSIONS_HIGH_MED,
    ENCODED_EMISSIONS_LOW,
    DEGRADED_EMISSIONS,
    COMBAT_AFTERMATH,
    RES_HAULAGE,
    RES_COMBAT,
    CZ_AUTHORITY;


    public static HorizonsMaterialSpawnLocation forName(final String name) {
        return HorizonsMaterialSpawnLocation.valueOf(name.toUpperCase());
    }

    @Override
    public String getLocalizationKey() {
        return "spawn.location.horizons." + this.name().toLowerCase();
    }
}
