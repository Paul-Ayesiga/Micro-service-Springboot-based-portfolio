<script setup>
// This is an example file showing how to use the table components
// You can create this file for reference and delete it after implementing your tables
import { createColumnHelper } from '@tanstack/vue-table'
import { 
  Table, 
  TableBody, 
  TableCaption, 
  TableCell, 
  TableHead, 
  TableHeader, 
  TableRow 
} from './index'

// Example 1: Basic Table
// This shows how to use the individual components directly

// Example 2: Data Table with @tanstack/vue-table
import DataTable from './data-table.vue'

// Define your columns
const columnHelper = createColumnHelper()
const columns = [
  columnHelper.accessor('name', {
    header: 'Name',
    cell: info => info.getValue(),
  }),
  columnHelper.accessor('email', {
    header: 'Email',
    cell: info => info.getValue(),
  }),
  columnHelper.accessor('role', {
    header: 'Role',
    cell: info => info.getValue(),
  }),
]

// Your data
const data = [
  { name: 'John Doe', email: 'john@example.com', role: 'Developer' },
  { name: 'Jane Smith', email: 'jane@example.com', role: 'Designer' },
  { name: 'Bob Johnson', email: 'bob@example.com', role: 'Manager' },
]
</script>

<template>
  <div class="space-y-10">
    <!-- Example 1: Basic Table -->
    <div>
      <h2 class="text-xl font-bold mb-4">Basic Table Example</h2>
      <Table>
        <TableCaption>A list of your recent invoices.</TableCaption>
        <TableHead>
          <TableRow>
            <TableHeader>Invoice</TableHeader>
            <TableHeader>Status</TableHeader>
            <TableHeader>Method</TableHeader>
            <TableHeader class="text-right">Amount</TableHeader>
          </TableRow>
        </TableHead>
        <TableBody>
          <TableRow>
            <TableCell class="font-medium">INV001</TableCell>
            <TableCell>Paid</TableCell>
            <TableCell>Credit Card</TableCell>
            <TableCell class="text-right">$250.00</TableCell>
          </TableRow>
          <TableRow>
            <TableCell class="font-medium">INV002</TableCell>
            <TableCell>Pending</TableCell>
            <TableCell>PayPal</TableCell>
            <TableCell class="text-right">$150.00</TableCell>
          </TableRow>
          <TableRow>
            <TableCell class="font-medium">INV003</TableCell>
            <TableCell>Unpaid</TableCell>
            <TableCell>Bank Transfer</TableCell>
            <TableCell class="text-right">$350.00</TableCell>
          </TableRow>
        </TableBody>
      </Table>
    </div>

    <!-- Example 2: Data Table -->
    <div>
      <h2 class="text-xl font-bold mb-4">Data Table Example</h2>
      <DataTable :columns="columns" :data="data" />
    </div>
  </div>
</template>

