# Mirror
A modern Java reflection library.

## Installation
### Gradle
```groovy
repositories {
	maven {
		name "shadowfacts"
		url "http://mvn.rx14.co.uk/shadowfacts/"
	}
}

dependencies {
	compile group: "net.shadowfacts", name: "Mirror", version: "1.0.0"
}
```

## Usage
Use on of the `Mirror.of` or `Mirror.ofAll` variations to obtain a `MirrorClass`, a `MirrorField`, a `MirrorMethod`, a `MirrorConstructor`, a `MirrorEnum`, or a stream of any of the above.

For more information, see the [JavaDocs](https://shadowfacts.net/Mirror/).