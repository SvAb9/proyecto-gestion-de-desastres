# Sistema Gestión de Desastres - v3 (DesaRecu)

Ejecución rápida:
1. mvn -U clean package
2. mvn test
3. mvn exec:java "-Dexec.mainClass=edu.universidad.vista.LoginView"
   (o doble clic en run.bat en Windows)

Login por defecto:
- usuario: admin
- contraseña: admin

Cambiar nombres de zonas:
- Edita src/main/resources/data/data.json y modifica el campo 'name' en cada objeto de 'zones'.

Documentación:
- Documentacion/Guia-DesaRecu.pdf (guía de usuario en español, lenguaje sencillo)
