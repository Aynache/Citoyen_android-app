package com.example.projetsession;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.projetsession.Database.AppDatabase;
import com.example.projetsession.databinding.FragmentForgotPasswordBinding;
import com.example.projetsession.models.Users;
import com.example.projetsession.security.PasswordGenerateur;

import java.util.function.Consumer;


public class ForgotPasswordFragment extends Fragment {

    private FragmentForgotPasswordBinding binding;


    public ForgotPasswordFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentForgotPasswordBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        Users myusers = new Users();

        binding.backToLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment loginFragment = new LoginFragment();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, loginFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        binding.resetpasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = binding.resetpasswordEditText.getText().toString();
                if (username != null && !username.isEmpty()) {
                    myusers.setUsername(username);
                    resetPassword(myusers, updatedUser -> {
                        if (updatedUser != null) {
                            myusers.setPassword(updatedUser.getPassword()); // Just update the password or other fields
                            showVariableInDialog(myusers.getPassword()); // Now showing the updated password
                        } else {
                            showVariableInDialog("User not found or password reset failed");
                        }
                    });
                }

            }
        });
    }

    private void resetPassword(Users user, Consumer<Users> callback) {
        AppDatabase db = AppDatabase.getDatabase(getContext());

        db.read(() -> {
            Users foundUser = db.getUsersDAO().findUserByUsername(user.getUsername());
            new Handler(Looper.getMainLooper()).post(() -> {
                if (foundUser != null) {
                    String newPassword = PasswordGenerateur.generateRandomPassword(7);
                    foundUser.setPassword(newPassword); // Set the new generated password
                    db.write(() -> db.getUsersDAO().updateUser(foundUser));
                    Log.i("ResetPassword", "Password reset for user: " + foundUser.getEmail());
                }
                // Execute callback with updated user
                callback.accept(foundUser);
            });
        });
    }


    private void showVariableInDialog(String variableValue) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Nouveau mot de passe généré");
        builder.setMessage(variableValue);
        builder.setPositiveButton("OK", (dialog, id) -> dialog.dismiss());
        builder.setNegativeButton("Cancel", (dialog, id) -> dialog.cancel());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

}