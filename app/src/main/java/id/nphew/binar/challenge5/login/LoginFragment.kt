package id.nphew.binar.challenge5.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import id.nphew.binar.challenge5.database.AccountDatabase
import id.nphew.binar.challenge5.databinding.FragmentLoginBinding
import id.nphew.binar.challenge5.repository.AccountRepo
import id.nphew.binar.challenge5.repository.viewModelsFactory
import id.nphew.binar.challenge5.sharedPrefs.SharedPrefs
import id.nphew.binar.challenge5.viewmodel.AccountViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private var accountDb: AccountDatabase? = null
    private lateinit var authenticator: SharedPrefs

    private val accRepo: AccountRepo by lazy { AccountRepo(requireContext()) }
    private val viewModel: AccountViewModel by viewModelsFactory { AccountViewModel(accRepo) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onStart() {
        super.onStart()
        if(authenticator.checkAuth())
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToHomeFragment())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        accountDb = AccountDatabase.getInstance(requireContext())
        authenticator = SharedPrefs(requireContext())

        login()
//        observeData()
        noAccount()
    }

    private fun login(){
        binding.btnLoginLogin.setOnClickListener {
            val email: String ; val password: String
            binding.apply {
                email = etLoginEmail.text.toString()
                password = etLoginPassword.text.toString()
            }

            if (email.isEmpty() || password.isEmpty())
                createToast("Pastikan Semua Field Terisi").show()
            else{
                CoroutineScope(Dispatchers.Main).launch {
                    if(isDbEmailExist(email) && password == getPwd(email)){
                        authenticator.setLoggedInUser(email, password)
                        it.findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToHomeFragment())
                    }else if(isDbEmailExist(email) && password != getPwd(email)){
                        createToast("Password yang Anda masukkan salah!").show()
                    }else
                        createToast("Email tidak terdaftar!").show()
                }
//                viewModel.checkLoginEmail(email)
            }
        }
    }
//    private fun observeData(){
//        viewModel.emailnotexist.observe(viewLifecycleOwner){
//            createToast("Email tidak terdaftar!").show()
//        }
//        viewModel.passnotmatch.observe(viewLifecycleOwner){
//            createToast("Password Salah!").show()
//        }
//        viewModel.passmatch.observe(requireActivity()){
//            authenticator.setLoggedInUser(email, password)
//            binding.root.findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToHomeFragment())
//        }
//    }

    private suspend fun isDbEmailExist(email: String): Boolean = withContext(Dispatchers.IO) {
        val emailDb = accountDb?.accountDao()?.checkEmailAccount(email)
        return@withContext !emailDb.isNullOrEmpty()
    }

    private suspend fun getPwd(email: String?): String? = withContext(Dispatchers.IO) {
        return@withContext accountDb?.accountDao()?.getPassword(email)
    }
    private fun noAccount(){
        binding.etLoginTdkpunyaakun.setOnClickListener{
            it.findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToRegisterFragment())
        }
    }
    private fun createToast(msg: String): Toast {
        return Toast.makeText(requireContext(), msg, Toast.LENGTH_LONG)
    }
}