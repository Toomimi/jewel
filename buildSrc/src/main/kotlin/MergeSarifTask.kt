import io.github.detekt.sarif4k.SarifSchema210
import io.github.detekt.sarif4k.Version
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.json.encodeToStream
import org.gradle.api.file.FileTree
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.IgnoreEmptyDirectories
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.SkipWhenEmpty
import org.gradle.api.tasks.SourceTask
import org.gradle.api.tasks.TaskAction

@CacheableTask
open class MergeSarifTask : SourceTask() {

    init {
        group = "verification"
    }

    @get:OutputFile
    val mergedSarifPath: RegularFileProperty = project.objects.fileProperty()
        .convention(project.layout.buildDirectory.file("reports/static-analysis.sarif"))

    @TaskAction
    fun merge() {
        val json = Json { prettyPrint = true }

        logger.lifecycle("Merging ${source.files.size} SARIF file(s)...")
        logger.lifecycle(source.files.joinToString("\n") { " *  ~${it.path.removePrefix(project.rootDir.path)}" })

        val merged = SarifSchema210(
            schema = "https://raw.githubusercontent.com/oasis-tcs/sarif-spec/master/Schemata/sarif-schema-2.1.0.json",
            version = Version.The210,
            runs = source.files
                .asSequence()
                .filter { it.extension == "sarif" }
                .map { file -> file.inputStream().use { json.decodeFromStream<SarifSchema210>(it) } }
                .flatMap { report -> report.runs }
                .groupBy { run -> run.tool.driver.guid ?: run.tool.driver.name }
                .values
                .asSequence()
                .filter { it.isNotEmpty() }
                .map { it.first().copy(results = it.flatMap { it.results ?: emptyList() }) }
                .toList()
        )
        logger.lifecycle("Merged SARIF file contains ${merged.runs.size} run(s)")
        logger.info("Writing merged SARIF file to $mergedSarifPath...")
        mergedSarifPath.asFile.get().outputStream()
            .use { json.encodeToStream(merged, it) }
    }
}
