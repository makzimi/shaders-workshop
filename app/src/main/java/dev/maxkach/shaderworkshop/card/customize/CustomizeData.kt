package dev.maxkach.shaderworkshop.card.customize

import kotlinx.collections.immutable.persistentListOf
import dev.maxkach.shaderworkshop.R

internal val CappuccinoCustomizeGroups = persistentListOf(
    CustomizeGroup(
        id = "altj",
        name = "Sprinkles",
        singleChoice = false,
        items = persistentListOf(
            CustomizeItem(
                id = "altj_ic_ing_sprinkles_cinnamon",
                name = "Cinnamon",
                image = R.drawable.ic_ing_sprinkles_cinnamon,
                price = null,
            ),
            CustomizeItem(
                id = "altj_ic_ing_sprinkles_powder_cocoa",
                name = "Powder Cocoa",
                image = R.drawable.ic_ing_sprinkles_powder_cocoa,
                price = null,
            ),
            CustomizeItem(
                id = "altj_ic_ing_sprinkles_marshmallow",
                name = "Marshmallow",
                image = R.drawable.ic_ing_sprinkles_marshmallow,
                price = "+ 30 ₽",
            ),
            CustomizeItem(
                id = "altj_ic_ing_sprinkles_cookie_crumble_topping",
                name = "Cookie crumble topping",
                image = R.drawable.ic_ing_sprinkles_cookie_crumble_topping,
                price = "+ 10 ₽",
            ),
            CustomizeItem(
                id = "altj_ic_ing_sprinkles_freeze_dried_raspberries",
                name = "Freeze-dried raspberries",
                image = R.drawable.ic_ing_sprinkles_freeze_dried_raspberries,
                price = "+ 25 ₽",
            ),
            CustomizeItem(
                id = "altj_ic_ing_sprinkles_raspberry_powder",
                name = "Raspberry powder",
                image = R.drawable.ic_ing_sprinkles_raspberry_powder,
                price = "+ 10 ₽",
            ),
            CustomizeItem(
                id = "altj_ic_ing_sprinkles_mango_topping",
                name = "Mango Topping",
                image = R.drawable.ic_ing_sprinkles_mango_topping,
                price = "+ 25 ₽",
            ),
            CustomizeItem(
                id = "altj_ic_ing_sprinkles_blueberry_ube_sprinkles",
                name = "Blueberry-ube sprinkles",
                image = R.drawable.ic_ing_sprinkles_blueberry_ube_sprinkles,
                price = "+ 10 ₽",
            ),
            CustomizeItem(
                id = "altj_ic_ing_sprinkles_flower_mix",
                name = "Flower mix",
                image = R.drawable.ic_ing_sprinkles_flower_mix,
                price = "+ 10 ₽",
            ),
        ),
    ),
    CustomizeGroup(
        id = "sbiv",
        name = "Healthy add-ons",
        singleChoice = false,
        items = persistentListOf(
            CustomizeItem(
                id = "sbiv_ic_ing_healthy_add_ons_vanilla_protein",
                name = "Vanilla protein",
                image = R.drawable.ic_ing_healthy_add_ons_vanilla_protein,
                price = null,
            ),
            CustomizeItem(
                id = "sbiv_ic_ing_healthy_add_ons_marine_collagen",
                name = "Marine Collagen",
                image = R.drawable.ic_ing_healthy_add_ons_marine_collagen,
                price = "+ 90 ₽",
            ),
            CustomizeItem(
                id = "sbiv_ic_ing_healthy_add_ons_mct_oil",
                name = "MCT Oil",
                image = R.drawable.ic_ing_healthy_add_ons_mct_oil,
                price = null,
            ),
        ),
    ),
    CustomizeGroup(
        id = "qyxo",
        name = "Foams and mousses",
        singleChoice = false,
        items = persistentListOf(
            CustomizeItem(
                id = "qyxo_ic_ing_foams_and_mousses_mango_foam",
                name = "Mango Foam",
                image = R.drawable.ic_ing_foams_and_mousses_mango_foam,
                price = "+ 95 ₽",
            ),
            CustomizeItem(
                id = "qyxo_ic_ing_foams_and_mousses_cheese_foam",
                name = "Cheese foam",
                image = R.drawable.ic_ing_foams_and_mousses_cheese_foam,
                price = "+ 95 ₽",
            ),
        ),
    ),
    CustomizeGroup(
        id = "txej",
        name = "Milk",
        singleChoice = true,
        items = persistentListOf(
            CustomizeItem(
                id = "txej_ic_ing_milk_cow_milk",
                name = "Cow milk",
                image = R.drawable.ic_ing_milk_cow_milk,
                price = null,
            ),
            CustomizeItem(
                id = "txej_ic_ing_milk_hazelnut_milk",
                name = "Hazelnut milk",
                image = R.drawable.ic_ing_milk_hazelnut_milk,
                price = "+ 75 ₽",
            ),
            CustomizeItem(
                id = "txej_ic_ing_milk_lactose_free_milk",
                name = "Lactose-free milk",
                image = R.drawable.ic_ing_milk_lactose_free_milk,
                price = "+ 75 ₽",
            ),
            CustomizeItem(
                id = "txej_ic_ing_milk_coconut_milk",
                name = "Coconut milk",
                image = R.drawable.ic_ing_milk_coconut_milk,
                price = "+ 75 ₽",
            ),
            CustomizeItem(
                id = "txej_ic_ing_milk_oat_milk",
                name = "Oat milk",
                image = R.drawable.ic_ing_milk_oat_milk,
                price = "+ 75 ₽",
            ),
            CustomizeItem(
                id = "txej_ic_ing_milk_banana_milk",
                name = "Banana milk",
                image = R.drawable.ic_ing_milk_banana_milk,
                price = "+ 75 ₽",
            ),
            CustomizeItem(
                id = "txej_ic_ing_milk_almond_milk",
                name = "Almond milk",
                image = R.drawable.ic_ing_milk_almond_milk,
                price = "+ 75 ₽",
            ),
            CustomizeItem(
                id = "txej_ic_ing_milk_cream",
                name = "Cream",
                image = R.drawable.ic_ing_milk_cream,
                price = "+ 40 ₽",
            ),
        ),
    ),
    CustomizeGroup(
        id = "vjqs",
        name = "Espresso",
        singleChoice = true,
        items = persistentListOf(
            CustomizeItem(
                id = "vjqs_ic_ing_espresso_espresso_shot",
                name = "Espresso shot",
                image = R.drawable.ic_ing_espresso_espresso_shot,
                price = null,
            ),
            CustomizeItem(
                id = "vjqs_ic_ing_espresso_espresso_decaf_shot",
                name = "Espresso decaf shot",
                image = R.drawable.ic_ing_espresso_espresso_decaf_shot,
                price = null,
            ),
        ),
    ),
    CustomizeGroup(
        id = "lrej",
        name = "Temperature",
        singleChoice = true,
        items = persistentListOf(
            CustomizeItem(
                id = "lrej_ic_ing_temperature_standard",
                name = "Standard",
                image = R.drawable.ic_ing_temperature_standard,
                price = null,
            ),
            CustomizeItem(
                id = "lrej_ic_ing_temperature_hotter",
                name = "Hotter",
                image = R.drawable.ic_ing_temperature_hotter,
                price = null,
            ),
        ),
    ),
    CustomizeGroup(
        id = "fxpr",
        name = "Toppings",
        singleChoice = false,
        items = persistentListOf(
            CustomizeItem(
                id = "fxpr_ic_ing_toppings_mango_sauce",
                name = "Mango Sauce",
                image = R.drawable.ic_ing_toppings_mango_sauce,
                price = "+ 25 ₽",
            ),
            CustomizeItem(
                id = "fxpr_ic_ing_toppings_salted_caramel_sau_e",
                name = "Salted caramel sauсe",
                image = R.drawable.ic_ing_toppings_salted_caramel_sau_e,
                price = "+ 60 ₽",
            ),
            CustomizeItem(
                id = "fxpr_ic_ing_toppings_taro_sauce",
                name = "Taro sauce",
                image = R.drawable.ic_ing_toppings_taro_sauce,
                price = null,
            ),
        ),
    ),
    CustomizeGroup(
        id = "yusz",
        name = "Sugar",
        singleChoice = false,
        items = persistentListOf(
            CustomizeItem(
                id = "yusz_ic_ing_sugar_sugar",
                name = "Sugar",
                image = R.drawable.ic_ing_sugar_sugar,
                price = null,
            ),
            CustomizeItem(
                id = "yusz_ic_ing_sugar_sugar_brown",
                name = "Sugar Brown",
                image = R.drawable.ic_ing_sugar_sugar_brown,
                price = null,
            ),
            CustomizeItem(
                id = "yusz_ic_ing_sugar_sweetener",
                name = "Sweetener",
                image = R.drawable.ic_ing_sugar_sweetener,
                price = null,
            ),
        ),
    ),
    CustomizeGroup(
        id = "edjz",
        name = "Syrups",
        singleChoice = false,
        items = persistentListOf(
            CustomizeItem(
                id = "edjz_ic_ing_syrups_syrup_vanilla",
                name = "Syrup Vanilla",
                image = R.drawable.ic_ing_syrups_syrup_vanilla,
                price = "+ 15 ₽",
            ),
            CustomizeItem(
                id = "edjz_ic_ing_syrups_syrup_caramel",
                name = "Syrup Caramel",
                image = R.drawable.ic_ing_syrups_syrup_caramel,
                price = "+ 15 ₽",
            ),
            CustomizeItem(
                id = "edjz_ic_ing_syrups_syrup_chestnut",
                name = "Syrup Chestnut",
                image = R.drawable.ic_ing_syrups_syrup_chestnut,
                price = "+ 15 ₽",
            ),
            CustomizeItem(
                id = "edjz_ic_ing_syrups_syrup_coconut",
                name = "Syrup Coconut",
                image = R.drawable.ic_ing_syrups_syrup_coconut,
                price = "+ 15 ₽",
            ),
            CustomizeItem(
                id = "edjz_ic_ing_syrups_syrup_raspberry",
                name = "Syrup Raspberry",
                image = R.drawable.ic_ing_syrups_syrup_raspberry,
                price = "+ 15 ₽",
            ),
            CustomizeItem(
                id = "edjz_ic_ing_syrups_syrup_mint",
                name = "Syrup Mint",
                image = R.drawable.ic_ing_syrups_syrup_mint,
                price = "+ 15 ₽",
            ),
            CustomizeItem(
                id = "edjz_ic_ing_syrups_syrup_banana",
                name = "Syrup Banana",
                image = R.drawable.ic_ing_syrups_syrup_banana,
                price = "+ 15 ₽",
            ),
            CustomizeItem(
                id = "edjz_ic_ing_syrups_syrup_ginger",
                name = "Syrup Ginger",
                image = R.drawable.ic_ing_syrups_syrup_ginger,
                price = "+ 15 ₽",
            ),
            CustomizeItem(
                id = "edjz_ic_ing_syrups_blackberry_syrup",
                name = "Blackberry syrup",
                image = R.drawable.ic_ing_syrups_blackberry_syrup,
                price = "+ 15 ₽",
            ),
        ),
    ),
    CustomizeGroup(
        id = "prso",
        name = "Shot of cocoa",
        singleChoice = false,
        items = persistentListOf(
            CustomizeItem(
                id = "prso_ic_ing_shot_of_cocoa_cocoa",
                name = "Cocoa",
                image = R.drawable.ic_ing_shot_of_cocoa_cocoa,
                price = "+ 25 ₽",
            ),
            CustomizeItem(
                id = "prso_ic_ing_shot_of_cocoa_cocoa_sugar",
                name = "Cocoa sugar",
                image = R.drawable.ic_ing_shot_of_cocoa_cocoa_sugar,
                price = null,
            ),
        ),
    ),
    CustomizeGroup(
        id = "jqjx",
        name = "Lid",
        singleChoice = true,
        items = persistentListOf(
            CustomizeItem(
                id = "jqjx_ic_ing_lid_lid",
                name = "Lid",
                image = R.drawable.ic_ing_lid_lid,
                price = null,
            ),
            CustomizeItem(
                id = "jqjx_ic_ing_lid_without_lid",
                name = "Without lid",
                image = R.drawable.ic_ing_lid_without_lid,
                price = null,
            ),
        ),
    ),
    CustomizeGroup(
        id = "etgf",
        name = "Cup",
        singleChoice = true,
        items = persistentListOf(
            CustomizeItem(
                id = "etgf_ic_ing_cup_cup",
                name = "Cup",
                image = R.drawable.ic_ing_cup_cup,
                price = null,
            ),
            CustomizeItem(
                id = "etgf_ic_ing_cup_own_cup",
                name = "Own cup",
                image = R.drawable.ic_ing_cup_own_cup,
                price = null,
            ),
        ),
    ),
    CustomizeGroup(
        id = "eejw",
        name = "Cup sleeve",
        singleChoice = true,
        items = persistentListOf(
            CustomizeItem(
                id = "eejw_ic_ing_cup_sleeve_without_cup_sleeve",
                name = "Without cup sleeve",
                image = R.drawable.ic_ing_cup_sleeve_without_cup_sleeve,
                price = null,
            ),
            CustomizeItem(
                id = "eejw_ic_ing_cup_sleeve_with_cup_sleeve",
                name = "With cup sleeve",
                image = R.drawable.ic_ing_cup_sleeve_with_cup_sleeve,
                price = null,
            ),
        ),
    ),
)
