rootProject.name = "section01"

dependencyResolutionManagement {
	versionCatalogs {
		create("libs") {
			from(files("../libs.versions.toml"))
		}
	}
}

// Include the base library as a composite build
includeBuild("../base")
