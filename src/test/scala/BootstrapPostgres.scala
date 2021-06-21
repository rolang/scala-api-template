import com.opentable.db.postgres.embedded.EmbeddedPostgres
import io.conduktor.api.config.DBConfig
import io.conduktor.api.repository.db.DbSessionPool
import zio._

object BootstrapPostgres {

  val pgLayer = ZLayer.fromManaged(ZManaged.make(Task(EmbeddedPostgres.start()))(pg => Task(pg.close()).orDie).map { pg =>
    DBConfig(
      user = "postgres",
      password = None,
      host = "localhost",
      port = pg.getPort,
      database = "postgres",
      maxPoolSize = 5,
      gcpInstance = None,
      ssl = false
    )
  })

  val dbLayer = pgLayer >>> DbSessionPool.layer
}
