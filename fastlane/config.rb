
class Config

    # Build type
    BUILD_TYPE_DEBUG = "debug"
    BUILD_TYPE_RELEASE = "release"

    # Gradle build task type
    BUILD_TASK_ASSEMBLE = "assemble"
    BUILD_TASK_BUNDLE = "bundle"

    # Path to generated versioning file (version name)
    VERSIONING_FILE_PATH = "../version.properties"

    # Version bump tasks
    BUMP_BUILD = "incrementBuild"
    BUMP_PATCH = "incrementPatch"
    BUMP_MINOR = "incrementMinor"
    BUMP_MAJOR = "incrementMajor"

    # Firebase app distribution application identifier.
    FIREBASE_APP_ID = "1:718845446517:android:5ff0471196027d8698ab3c"
    # Firebase distribution bucket url
    FIREBASE_DISTRIBUTION_URL = "https://appdistribution.firebase.google.com/testerapps"

    # Project repository git url
    GIT_REPO = "git@github.com:Blondhino/GF_android.git"
    GIT_REPO_TOKEN_URL_SUFFIX = "@github.com/Blondhino/GF_android.git"

    #Firebase
    TESTERS_GROUP = "apartment-6"

end