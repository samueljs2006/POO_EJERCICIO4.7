class Cuenta(val numeroCuenta: String, var saldo: Double = 0.0) {
    fun consultarSaldo(): Double {
        return saldo
    }

    fun abonar(cantidad: Double) {
        saldo += cantidad
    }

    fun pagar(cantidad: Double): Boolean {
        return if (saldo >= cantidad) {
            saldo -= cantidad
            true
        } else {
            false
        }
    }

    companion object {
        fun esMorosa(persona: Persona): Boolean {
            return persona.cuentas.any { it?.saldo ?: 0.0 < 0 }
        }

        fun transferencia(persona1: Persona, idCuenta1: String, persona2: Persona, idCuenta2: String, cantidad: Double): Boolean {
            val cuentaOrigen = persona1.cuentas.find { it?.numeroCuenta == idCuenta1 }
            val cuentaDestino = persona2.cuentas.find { it?.numeroCuenta == idCuenta2 }

            return if (cuentaOrigen != null && cuentaDestino != null && cuentaOrigen.saldo >= cantidad) {
                cuentaOrigen.pagar(cantidad)
                cuentaDestino.abonar(cantidad)
                true
            } else {
                false
            }
        }
    }
}

class Persona(val dni: String) {
    val cuentas = arrayOfNulls<Cuenta>(3)

    fun agregarCuenta(cuenta: Cuenta): Boolean {
        for (i in cuentas.indices) {
            if (cuentas[i] == null) {
                cuentas[i] = cuenta
                return true
            }
        }
        return false
    }
}

fun main() {
    val persona = Persona("12345678A")
    val cuenta1 = Cuenta("001", 0.0)
    val cuenta2 = Cuenta("002", 700.0)

    persona.agregarCuenta(cuenta1)
    persona.agregarCuenta(cuenta2)

    // Ingreso de nómina
    cuenta1.abonar(1100.0)
    // Pago de alquiler
    cuenta2.pagar(750.0)

    println("¿La persona es morosa?: ${Cuenta.esMorosa(persona)}")

    // Realizar transferencia para que no sea morosa
    Cuenta.transferencia(persona, "001", persona, "002", 200.0)

    println("¿La persona es morosa después de la transferencia?: ${Cuenta.esMorosa(persona)}")
    println("Saldo cuenta 1: ${cuenta1.consultarSaldo()}")
    println("Saldo cuenta 2: ${cuenta2.consultarSaldo()}")
}
