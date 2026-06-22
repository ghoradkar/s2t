package com.bioland.bledemo.smart_scale.utils

data class BodyCompositionInput(
    val height: Int,     // cm
    val weight: Double,    // kg (already in double)
    val impedance: Int,  // Ohms
    val sex: Int,        // 1 = Male, 0 = Female
    val age: Int
)

data class BodyCompositionOutput(
    val bfp: Double,   // Body Fat Percentage
    val slm: Double,   // Skeletal Lean Mass
    val bwp: Double,   // Body Water Percentage
    val bmc: Double,   // Bone Mineral Content
    val vfr: Double,   // Visceral Fat Rating
    val pp: Double,    // Protein Percentage
    val smm: Double,   // Skeletal Muscle Mass
    val bmr: Double,   // Basal Metabolic Rate
    val bmi: Double,   // Body Mass Index
    val sbw: Double,   // Standard Body Weight
    val mc: Double,    // Muscle Control
    val wc: Double,    // Weight Control
    val fc: Double,    // Fat Control
    val metabolicAge: Int,  // Metabolic Age
    val score: Int     // Health Score
)
fun calculateBodyComposition(input: BodyCompositionInput): BodyCompositionOutput {
    val heightM = input.height / 100.0  // Convert cm to meters
    val weightKg = input.weight         // Already in kg

    // Body Mass Index (BMI)
    val bmi = weightKg / (heightM * heightM)

    // Basal Metabolic Rate (BMR) - Harris-Benedict Equation
    val bmr = if (input.sex == 1) {
        88.36 + (13.4 * weightKg) + (4.8 * input.height) - (5.7 * input.age)
    } else {
        447.6 + (9.2 * weightKg) + (3.1 * input.height) - (4.3 * input.age)
    }

    // Body Fat Percentage (BFP) - Estimation using impedance
    val bfp = if (input.sex == 1) {
        1.20 * bmi + 0.23 * input.age - 16.2
    } else {
        1.20 * bmi + 0.23 * input.age - 5.4
    }

    // Skeletal Lean Mass (SLM) - Approximation
    val slm = weightKg * (1 - (bfp / 100))

    // Body Water Percentage (BWP)
    val bwp = if (input.sex == 1) 0.6 * slm else 0.5 * slm

    // Bone Mineral Content (BMC)
    val bmc = weightKg * 0.05

    // Visceral Fat Rating (VFR)
    val vfr = if (bfp > 25) (bfp - 20) else 1.0

    // Protein Percentage (PP)
    val pp = slm * 0.2

    // Skeletal Muscle Mass (SMM)
    val smm = weightKg * 0.35

    // Standard Body Weight (SBW) - Ideal weight based on BMI of 22
    val sbw = 22.0 * heightM * heightM

    // Muscle Control (MC), Weight Control (WC), Fat Control (FC)
    val mc = slm - sbw * 0.8
    val wc = weightKg - sbw
    val fc = wc - mc

    // Metabolic Age - Based on BMR (estimated)
    val metabolicAge = ((bmr / 10) + input.age - 20).toInt()

    // Score (Health Score)
    val score = (100 - (bfp * 1.5)).toInt().coerceIn(0, 100)

    return BodyCompositionOutput(
        bfp, slm, bwp, bmc, vfr, pp, smm, bmr, bmi, sbw, mc, wc, fc, metabolicAge, score
    )
}

