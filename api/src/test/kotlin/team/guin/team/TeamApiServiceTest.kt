package team.guin.team

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import team.guin.account.AccountApiRepository
import team.guin.domain.team.HashTag
import team.guin.domain.team.TeamRole
import team.guin.domain.team.TeamTemplate
import team.guin.domain.team.dto.TeamCreate
import team.guin.domain.team.enumeration.SubjectType
import team.guin.domain.team.enumeration.TagType
import team.guin.domain.team.enumeration.TemplateType
import team.guin.util.createAccount

@SpringBootTest
class TeamApiServiceTest(
    private val accountApiRepository: AccountApiRepository,
    private val teamApiService: TeamApiService,
    private val teamApiRepository: TeamApiRepository,
) : FreeSpec({
    "create" - {
        "템플릿, 해시태그, 역할, 주제, 오픈채팅 주소를 작성하면 팀이 생성된다." - {
            // given
            val account = accountApiRepository.createAccount()
            val templates: List<TeamTemplate> = listOf(
                TeamTemplate(TemplateType.TEXT, "test", null),
                TeamTemplate(
                    TemplateType.CHECKBOX,
                    "test2",
                    "네,아니오",
                ),
            )
            val subject = "9in"
            val subjectType = SubjectType.PROJECT
            val content = "9in 프로젝트 구해요"
            val openChatUrl = "http://9in-proejct.chat"
            val roles = listOf(TeamRole.create("백엔드", 2))
            val hashTags =
                listOf(
                    HashTag(TagType.JAVA),
                    HashTag(TagType.MYSQL),
                )
            val teamCreate = TeamCreate(
                subject = subject,
                content = content,
                openChatUrl = openChatUrl,
                templates = templates,
                hashTags = hashTags,
                subjectType = subjectType,
                roles = roles,
            )

            // when
            val team = teamApiService.createTeam(account.id, teamCreate)

            // then
            team.content shouldBe content
            team.openChatUrl shouldBe openChatUrl
            team.hashTags.size shouldBe teamCreate.hashTags.size
            team.templates.size shouldBe teamCreate.templates.size
            team.roles.size shouldBe teamCreate.roles.size
        }
        "모집글이 삭제되면 템플릿, 역할, 해시태그가 같이 삭제된다." - {
            // given
            val account = accountApiRepository.createAccount(nickname = "nickname2", email = "asd@aaa.com")
            val templates: List<TeamTemplate> = listOf(
                TeamTemplate(TemplateType.TEXT, "test", null),
                TeamTemplate(
                    TemplateType.CHECKBOX,
                    "test2",
                    "네,아니오",
                ),
            )
            val subject = "9in"
            val subjectType = SubjectType.PROJECT
            val content = "9in 프로젝트 구해요"
            val openChatUrl = "http://9in-proejct.chat"
            val roles = listOf(TeamRole.create("백엔드", 2))
            val hashTags =
                listOf(
                    HashTag(TagType.JAVA),
                    HashTag(TagType.MYSQL),
                )
            val teamCreate = TeamCreate(
                subject = subject,
                content = content,
                openChatUrl = openChatUrl,
                templates = templates,
                hashTags = hashTags,
                subjectType = subjectType,
                roles = roles,
            )

            val team = teamApiService.createTeam(account.id, teamCreate)
            val saveTeamId = team.id

            // when
            teamApiRepository.delete(team)

            // then
            val findTeam = teamApiRepository.findByIdOrNull(saveTeamId)
            findTeam shouldBe null
        }
    }
})
