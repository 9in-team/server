package team.guin.account

import org.springframework.web.bind.annotation.*
import team.guin.account.dto.AccountDetail
import team.guin.account.dto.AccountUpdateRequest
import team.guin.common.CommonResponse

@RestController
@RequestMapping("/account")
class AccountApiController(
    private val accountApiService: AccountApiService,
) {

    @PutMapping("{id}")
    fun update(@PathVariable id: Long, @RequestBody accountUpdateRequest: AccountUpdateRequest): CommonResponse<AccountDetail> {
        val account = accountApiService.updateInfo(id, accountUpdateRequest.nickname, accountUpdateRequest.imageId)
        return CommonResponse.okWithDetail(AccountDetail(account.email, account.nickname, account.imageUrl))
    }

    @DeleteMapping("{id}")
    fun delete(@PathVariable id: Long): CommonResponse<Unit> {
        accountApiService.delete(id)
        return CommonResponse.ok()
    }
}
