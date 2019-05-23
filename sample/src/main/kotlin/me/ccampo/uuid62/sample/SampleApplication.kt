package me.ccampo.uuid62.sample

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.ser.std.UUIDSerializer
import me.ccampo.uuid62.core.util.toBase62String
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.net.URI
import java.util.UUID

@SpringBootApplication
class SampleApplication

data class UUID62(val id: UUID, @JsonSerialize(using = UUIDSerializer::class) val longId: UUID = id)

@RestController
@RequestMapping("/uuids")
class SampleController {

    val uuids = mutableSetOf<UUID>()

    @PostMapping
    fun addUUID(@RequestParam id: UUID): ResponseEntity<UUID62> {
        if (uuids.contains(id)) return ResponseEntity.status(HttpStatus.CONFLICT).build()
        uuids.add(id)
        return ResponseEntity.created(URI("/uuids/${id.toBase62String()}")).body(UUID62(id))
    }

    @PostMapping("/random")
    fun addRandomUUID(): ResponseEntity<UUID62> = addUUID(UUID.randomUUID())

    @GetMapping("/{id}")
    fun getUUID(@PathVariable id: UUID): ResponseEntity<UUID62> {
        if (!uuids.contains(id)) return ResponseEntity.notFound().build()
        return ResponseEntity.ok(UUID62(uuids.first { it == id }))
    }

    @GetMapping
    fun getAllUUIDs(): List<UUID62> = uuids.map { UUID62(it) }

    @DeleteMapping("/{id}")
    fun deleteUUID(@PathVariable id: UUID): ResponseEntity<Unit> {
        if (!uuids.contains(id)) return ResponseEntity.notFound().build()
        uuids.remove(id)
        return ResponseEntity.noContent().build()
    }
}

fun main(args: Array<String>) {
    runApplication<SampleApplication>(*args)
}
