package id.nphew.binar.challenge5.settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import id.nphew.binar.challenge5.database.AccountDatabase
import id.nphew.binar.challenge5.database.Profile
import id.nphew.binar.challenge5.databinding.FragmentProfileBinding
import id.nphew.binar.challenge5.repository.AccountRepo
import id.nphew.binar.challenge5.repository.viewModelsFactory
import id.nphew.binar.challenge5.sharedPrefs.SharedPrefs
import id.nphew.binar.challenge5.viewmodel.AccountViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var authenticator: SharedPrefs
    private var accountDb: AccountDatabase? = null
    private var accEmail: String? = null

    private val accRepo: AccountRepo by lazy { AccountRepo(requireContext()) }
    private val viewModel: AccountViewModel by viewModelsFactory { AccountViewModel(accRepo) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        accountDb = AccountDatabase.getInstance(requireContext())
        authenticator = SharedPrefs(requireContext())
        accEmail = authenticator.loggedInEmail()

        setProfile()
        updateProfile()
        logout()
    }

    private fun logout(){
        binding.btnProfileLogout.setOnClickListener {
            authenticator.clearLoggedInUser()
            it.findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToLoginFragment())
        }
    }
    private fun createProfile(){
        binding.btnProfileUpdate.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                val accountId = getAccountId(accEmail)

                val fullname = ""
                val birthdate = ""
                val address = ""

                val profile = Profile(
                    getProfileId(accountId),
                    accountId,
                    fullName = fullname,
                    birthDate = birthdate,
                    address = address
                )
                saveToDb(profile)
            }
        }
    }

    private fun updateProfile(){
        binding.btnProfileUpdate.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                val accountId = getAccountId(accEmail)
                val listProfile = getProfile(accountId)

                if(listProfile.isNullOrEmpty())
                    createProfile()
                else {
                    val username = binding.etProfileUsername.text.toString()
                    val fullname = binding.etProfileFullname.text.toString()
                    val birthdate = binding.etProfileBirthdate.text.toString()
                    val address = binding.etProfileAddress.text.toString()

                    val profile = Profile(
                        getProfileId(accountId),
                        accountId,
                        fullName = fullname,
                        birthDate = birthdate,
                        address = address
                    )
                    updateToDb(profile, username)
                }
            }
        }
    }

    private fun setProfile(){
        CoroutineScope(Dispatchers.Main).launch {
            val accId = getAccountId(accEmail)
            val username = getUsername(accEmail)

            val fullname = getProfileFullname(accId)
            val birthdate = getProfileBirthdate(accId)
            val address = getProfileAddress(accId)

            binding.apply {
                etProfileUsername.setText(username)
                etProfileFullname.setText(fullname)
                etProfileBirthdate.setText(birthdate)
                etProfileAddress.setText(address)
            }
        }
    }

    private fun updateToDb(profile: Profile, username: String){
        CoroutineScope(Dispatchers.IO).launch {
            val resultProfile = accountDb?.profileDao()?.updateProfile(profile)
            val resultAccount = accountDb?.accountDao()?.updateUsername(accEmail, username)
            setProfile()
            if (resultProfile != 0 && resultAccount != 0) {
                CoroutineScope(Dispatchers.Main).launch {
                    Toast.makeText(requireContext(), "Berhasil Diupdate", Toast.LENGTH_SHORT).show()
                }
            } else {
                CoroutineScope(Dispatchers.Main).launch {
                    Toast.makeText(requireContext(), "Gagal Diupdate", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun saveToDb(profile: Profile){
        CoroutineScope(Dispatchers.IO).launch {
            val result = accountDb?.profileDao()?.insertProfile(profile)
            setProfile()
            if (result != 0L) {
                CoroutineScope(Dispatchers.Main).launch {
                    Toast.makeText(requireContext(), "Berhasil Ditambahkan", Toast.LENGTH_SHORT).show()
                }
            } else {
                CoroutineScope(Dispatchers.Main).launch {
                    Toast.makeText(requireContext(), "Gagal Ditambahkan", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    private suspend fun getProfile(accId: Int?): List<Profile>? = withContext(Dispatchers.IO) {
        return@withContext accountDb?.profileDao()?.getProfile(accId)
    }

    private suspend fun getAccountId(email: String?): Int? = withContext(Dispatchers.IO) {
        return@withContext accountDb?.accountDao()?.getId(email)
    }

    private suspend fun getProfileId(accId: Int?): Int? = withContext(Dispatchers.IO) {
        return@withContext accountDb?.profileDao()?.getPK(accId)
    }

    private suspend fun getUsername(email: String?): String? = withContext(Dispatchers.IO) {
        return@withContext accountDb?.accountDao()?.getUsername(email)
    }
    private suspend fun getProfileFullname(accId: Int?): String? = withContext(Dispatchers.IO) {
        return@withContext accountDb?.profileDao()?.getFullname(accId)
    }
    private suspend fun getProfileBirthdate(accId: Int?): String? = withContext(Dispatchers.IO) {
        return@withContext accountDb?.profileDao()?.getBirthdate(accId)
    }
    private suspend fun getProfileAddress(accId: Int?): String? = withContext(Dispatchers.IO) {
        return@withContext accountDb?.profileDao()?.getAddress(accId)
    }
}