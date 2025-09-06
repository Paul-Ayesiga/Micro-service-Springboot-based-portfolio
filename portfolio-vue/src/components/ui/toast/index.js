import { ref, onUnmounted } from 'vue';

// Toast state
const toasts = ref([]);
let toastTimeout = null;
let toastId = 0;

// Toast function
export function useToast() {
  // Clean up on component unmount
  onUnmounted(() => {
    if (toastTimeout) {
      clearTimeout(toastTimeout);
    }
  });

  // Add a toast
  const toast = ({ title, description, variant = 'default', duration = 5000 }) => {
    const id = ++toastId;
    
    // Add toast to the array
    toasts.value.push({
      id,
      title,
      description,
      variant,
    });
    
    // Remove toast after duration
    setTimeout(() => {
      dismissToast(id);
    }, duration);
    
    return id;
  };

  // Dismiss a toast by id
  const dismissToast = (id) => {
    const index = toasts.value.findIndex(toast => toast.id === id);
    if (index !== -1) {
      toasts.value.splice(index, 1);
    }
  };

  // Dismiss all toasts
  const dismissAllToasts = () => {
    toasts.value = [];
  };

  return {
    toasts,
    toast,
    dismissToast,
    dismissAllToasts
  };
}
