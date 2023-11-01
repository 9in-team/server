package team.guin.wish

import org.springframework.stereotype.Service
import team.guin.account.AccountApiRepository
import team.guin.domain.wish.Wish
import team.guin.team.TeamApiRepository

@Service
class WishApiService(
    private val wishApiRepository: WishApiRepository,
    private val accountApiRepository: AccountApiRepository,
    private val teamApiRepository: TeamApiRepository,
) {
    fun create(accountId: Long, wishCreateRequest: WishCreateRequest): Wish {
        val account = accountApiRepository.findById(accountId)
            .orElseThrow { IllegalArgumentException("Account not found for ID: $accountId") }
        val team = teamApiRepository.findById(wishCreateRequest.teamId)
            .orElseThrow { IllegalArgumentException("Team not found for ID: ${wishCreateRequest.teamId}") }

        val wish = Wish.create(account, team)
        return wishApiRepository.save(wish)
    }

    fun delete(accountId: Long, wishId: Long) {
        val wish = wishApiRepository.findById(wishId)
            .orElseThrow { IllegalArgumentException("Wish not found for ID: $wishId") }

        if (wish.id != accountId) {
            throw AccessDeniedException("본인이 아닌 Wish를 삭제할 수 없습니다.")
        }
        return wishApiRepository.deleteByIdAndAccountId(wishId, accountId)
    }
}
