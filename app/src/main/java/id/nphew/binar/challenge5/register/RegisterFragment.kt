package id.nphew.binar.challenge5.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import id.nphew.binar.challenge5.database.Account
import id.nphew.binar.challenge5.databinding.FragmentRegisterBinding
import id.nphew.binar.challenge5.repository.AccountRepo
import id.nphew.binar.challenge5.repository.viewModelsFactory
import id.nphew.binar.challenge5.viewmodel.AccountViewModel

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val accRepo: AccountRepo by lazy { AccountRepo(requireContext()) }
    private val viewModel: AccountViewModel by viewModelsFactory { AccountViewModel(accRepo) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        registerAccount()
        observeData()
    }

    private fun registerAccount(){
        binding.btnRegisterDaftar.setOnClickListener {
            val username: String ; val email: String
            val password: String ; val confPassword: String
            binding.apply {
                username = etRegisterUsername.text.toString()
                email = etLoginEmail.text.toString()
                password = etLoginPassword.text.toString()
                confPassword = etLoginConfirmpassword.text.toString()
            }

            if (email.isEmpty() || username.isEmpty() || password.isEmpty() || confPassword.isEmpty())
                createToast("Pastikan Semua Field Terisi").show()
            else if (password != confPassword)
                createToast("Password Tidak Cocok!").show()
            else {
                val account = Account(null ,username, email, password)
                viewModel.checkEmail(email, account)
            }
        }
    }

    private fun observeData() {
        viewModel.insert.observe(viewLifecycleOwner) {
            createToast("Registrasi Sukses!").show()
            binding.root.findNavController().navigate(RegisterFragmentDirections.actionRegisterFragmentToLoginFragment())
        }

        viewModel.registered.observe(viewLifecycleOwner) {
            createToast("Email sudah terdaftar!").show()
        }
    }

    private fun createToast(msg: String): Toast {
        return Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT)
    }
}