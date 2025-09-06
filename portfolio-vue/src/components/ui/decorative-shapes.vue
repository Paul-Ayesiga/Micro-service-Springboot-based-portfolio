<template>
  <!-- Top-right decorative shape -->
  <div class="absolute -top-20 -right-20 w-96 h-96 bg-gradient-to-br from-blue-500/30 to-purple-500/30 rounded-full blur-3xl opacity-50 dark:from-blue-600/20 dark:to-purple-600/20"></div>
  
  <!-- Bottom-left decorative shape -->
  <div class="absolute -bottom-32 -left-32 w-96 h-96 bg-gradient-to-tr from-green-500/20 to-blue-500/20 rounded-full blur-3xl opacity-50 dark:from-green-600/10 dark:to-blue-600/10"></div>
  
  <!-- Middle-right semicircle -->
  <div class="absolute right-0 top-1/3 w-32 h-64 bg-gradient-to-l from-blue-400/20 to-transparent rounded-l-full blur-xl opacity-60 dark:from-blue-500/10"></div>
  
  <!-- Middle-left semicircle -->
  <div class="absolute left-0 top-2/3 w-32 h-64 bg-gradient-to-r from-purple-400/20 to-transparent rounded-r-full blur-xl opacity-60 dark:from-purple-500/10"></div>
  
  <!-- Small floating circles -->
  <div class="absolute top-1/4 left-1/4 w-16 h-16 bg-gradient-to-br from-amber-400/30 to-pink-400/30 rounded-full blur-xl opacity-70 dark:from-amber-500/20 dark:to-pink-500/20"></div>
  <div class="absolute bottom-1/4 right-1/4 w-24 h-24 bg-gradient-to-tr from-teal-400/30 to-blue-400/30 rounded-full blur-xl opacity-70 dark:from-teal-500/20 dark:to-blue-500/20"></div>
  
  <!-- Dotted pattern (optional) -->
  <div class="absolute inset-0 bg-repeat opacity-5 dark:opacity-3" style="background-image: radial-gradient(circle, currentColor 1px, transparent 1px); background-size: 24px 24px;"></div>
</template>

<script setup>
// This is a purely presentational component with no logic
</script>
