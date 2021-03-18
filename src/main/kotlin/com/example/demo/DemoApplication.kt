package com.example.demo


import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import java.security.MessageDigest
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec


@SpringBootApplication
class DemoApplication

fun main(args: Array<String>) {
	runApplication<DemoApplication>(*args)

}

@RestController
class RestControler{

		var key="contraseña"
		var usuario=""
		var contrasenaUser=""
		var cifrado=""
		var cifradoUser=""
		var contrasenaPorId=""

	@GetMapping("/suma/{n1}/{n2}")
			/*fun sumaNumeros(@PathVariable n1 : Int, @PathVariable n2 : Int) : Int {
                LoadDatabase.logger.info("Sumando... $n1, $n2")
                return n1 + n2
            }*/
	fun sumaNumeros(@PathVariable nombre : String, @PathVariable contrasena: String) : String {
		//LoadDatabase.logger.info("Sumando... $n1, $n2")

		usuario=getNombre()
		//Cifrar contraseña
		contrasenaUser=getContrasena()

		//Cifrar la contraseña
		val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
		cipher.init(Cipher.ENCRYPT_MODE,key(key))
		cifrado = Base64.getEncoder().encodeToString(cipher.doFinal(contrasenaUser?.toByteArray(Charsets.UTF_8)))


		//usuario --> Usuario
		//cifrado --> Contraseña cifrada
		getContrasenaUser(usuario,cifrado)

		//Usuario --> Cifrar --> cifradoUser
		//Contrasena --> cifrado


		//Cifrar usuario
		val cipherUser = Cipher.getInstance("AES/ECB/PKCS5Padding")
		cipher.init(Cipher.ENCRYPT_MODE,key(key))
		cifradoUser = Base64.getEncoder().encodeToString(cipher.doFinal(contrasenaUser?.toByteArray(Charsets.UTF_8)))

		//cifradoUser --> usuario cifrado
		//cifrado --> contraseña cifrada
		recibirClavesId(cifradoUser,cifrado)

		return  ("Nombre: "+getNombre()+" Contrasena: "+getContrasena())
	}

	fun getNombre():String{

		return "Juan"
	}

	fun getContrasena(): String{

		return (getNombre()+"123")
	}

	//Pasa el nombre del usuario y su contraseña cifrada para devolver la contraseña descifrada
	fun getContrasenaUser(nombre: String, contrasena: String): String{

		val cipher2 = Cipher.getInstance("AES/ECB/PKCS5Padding")
		cipher2.init(Cipher.DECRYPT_MODE, key(key));

		return String(cipher2.doFinal(Base64.getDecoder().decode(cifrado)))
	}


	fun recibirClavesId(cifradoUser:String, cifrado:String){

		val usuarioIntro= readLine()//El admin escribe el usuario

		if (usuarioIntro==getNombre()) {
			//Si lo encuentra desciframos el nombre para poder pasarlo por parametro a la función anterior para descifrar contraseñas
			var cipherUser = Cipher.getInstance("AES/ECB/PKCS5Padding")
			cipherUser.init(Cipher.DECRYPT_MODE, key(key));

			var userDescifrado = String(cipherUser.doFinal(Base64.getDecoder().decode(cifrado)))

			//Contraseña del usuario introducido
			var contrasenaUser =getContrasenaUser(userDescifrado,cifrado)

		}

			//contrasenaPorId= getContrasena()

	}



	fun key(key:String): SecretKeySpec {
		var aux1 = key.toByteArray(Charsets.UTF_8)
		val aux2 = MessageDigest.getInstance("SHA-1")
		aux1 = aux2.digest(aux1)
		aux1 = aux1.copyOf(16)
		return SecretKeySpec(aux1, "AES")


	}



}


data class Persona(var nombre: String, var contrasena: String)
data class Admin(var nombreUser: String, var contraseñaAdmin: String)