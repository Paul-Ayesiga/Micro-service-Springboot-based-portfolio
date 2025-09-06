<script setup>
import { ref } from 'vue'
import { 
  Dialog, 
  DialogTrigger, 
  DialogContent, 
  DialogHeader, 
  DialogFooter, 
  DialogTitle, 
  DialogDescription,
  DialogClose 
} from './'

// You can use this for controlled dialog state
const isOpen = ref(false)

const toggleDialog = () => {
  isOpen.value = !isOpen.value
}
</script>

<template>
  <div class="flex flex-col space-y-6">
    <!-- Example 1: Basic Dialog -->
    <div>
      <h2 class="text-xl font-bold mb-4">Basic Dialog Example</h2>
      
      <Dialog>
        <DialogTrigger class="bg-primary text-primary-foreground hover:bg-primary/90 h-10 px-4 py-2 rounded-md">
          Open Dialog
        </DialogTrigger>
        <DialogContent>
          <DialogHeader>
            <DialogTitle>Dialog Title</DialogTitle>
            <DialogDescription>
              This is a description of the dialog. It provides context about what the dialog is for.
            </DialogDescription>
          </DialogHeader>
          <p class="py-4">
            Your dialog content goes here. This can be any valid content you want to display.
          </p>
          <DialogFooter>
            <button class="bg-primary text-primary-foreground hover:bg-primary/90 h-10 px-4 py-2 rounded-md">
              Save changes
            </button>
          </DialogFooter>
          <DialogClose />
        </DialogContent>
      </Dialog>
    </div>

    <!-- Example 2: Controlled Dialog (optional) -->
    <div>
      <h2 class="text-xl font-bold mb-4">Controlled Dialog Example</h2>
      
      <button 
        @click="toggleDialog"
        class="bg-secondary text-secondary-foreground hover:bg-secondary/80 h-10 px-4 py-2 rounded-md"
      >
        Open Controlled Dialog
      </button>
      
      <Dialog v-model:open="isOpen">
        <DialogContent>
          <DialogHeader>
            <DialogTitle>Controlled Dialog</DialogTitle>
            <DialogDescription>
              This dialog's open state is controlled externally by a reactive variable.
            </DialogDescription>
          </DialogHeader>
          <div class="py-4">
            <p>This dialog can be opened and closed programmatically.</p>
          </div>
          <DialogFooter>
            <button 
              @click="toggleDialog"
              class="bg-destructive text-destructive-foreground hover:bg-destructive/90 h-10 px-4 py-2 rounded-md"
            >
              Close Dialog
            </button>
          </DialogFooter>
          <DialogClose />
        </DialogContent>
      </Dialog>
    </div>
  </div>
</template>

